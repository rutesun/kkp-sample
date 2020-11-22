package com.rutesun.core.domain

import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class MoneyDistribution private constructor(token: Token, creator: User, chatRoom: ChatRoom, totalAmount: Long) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    val chatRoom: ChatRoom = chatRoom

    @OneToMany(mappedBy = "distribution", cascade = [CascadeType.ALL])
    var items: List<DistributionItem> = emptyList()
        private set

    @ManyToOne
    @JoinColumn(name = "creator_id")
    val creator: User = creator

    val totalAmount = totalAmount

    @Column(unique = true)
    val token: Token = token

    @Enumerated(EnumType.STRING)
    var status: DistributionResourceStatus = DistributionResourceStatus.AVAILABLE
        private set

    // 분배 마감 시한
    private val closedAt: LocalDateTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES)

    private fun List<DistributionItem>.findReceiveRecord(receiver: User): DistributionItem? = this.find { it.userId == receiver.id }

    val completedAmount: Long
        get() {
            var sum = 0L
            items.filter { it.used }.forEach { sum += it.amount }
            return sum
        }

    // 분배가 마감
    val isClosed: Boolean
        get() = LocalDateTime.now().isAfter(closedAt)

    val isEmpty: Boolean
        get() = status == DistributionResourceStatus.EXHAUSTED

    val isExpired: Boolean
        get() = createdAt.plusDays(7).isBefore(LocalDateTime.now())

    fun receiveAny(receiver: User): DistributionItem? {
        // 만료된 건은 받을 수 없다.
        if (isClosed) throw ExpiredDistributionException(this)
        // 이미 받은 적 있는 사람은 받을 수 없다.
        val receivedRecord = items.findReceiveRecord(receiver)
        if (receivedRecord != null) throw AlreadyReceivedException(receivedRecord)
        // 같은 채팅룸에 속한 사람만 받을 수 있다.
        if (!chatRoom.checkJoined(receiver)) throw NotJoinedUser(receiver)

        val item = items.find { !it.used }
        if (item == null) {
            status = DistributionResourceStatus.EXHAUSTED
            return null
        }
        return item.apply { receive(receiver) }.also { log.info("receiver(id=${receiver.id}) 뿌리기 받음") }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this.javaClass)

        fun make(token: Token, creator: User, amount: Long, distributeCnt: Int, chatRoom: ChatRoom): MoneyDistribution {
            val perAmount = amount / distributeCnt
            val remain = amount % distributeCnt

            return MoneyDistribution(token, creator, chatRoom, amount).apply {
                val items = ArrayList<DistributionItem>(distributeCnt)
                for (i in 1..distributeCnt) {
                    if (i == distributeCnt) {
                        items.add(DistributionItem(this, perAmount + remain))
                    } else
                        items.add(DistributionItem(this, perAmount))
                }
                this.items = items
            }
        }
    }
}