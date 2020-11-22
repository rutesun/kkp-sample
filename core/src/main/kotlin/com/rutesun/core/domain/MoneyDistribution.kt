package com.rutesun.core.domain

import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class MoneyDistribution private constructor(creator: User, chatRoom: ChatRoom, totalAmount: Long) {
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

    private val expiredAt: LocalDateTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES)

    private fun List<DistributionItem>.findReceiveRecord(receiver: User): DistributionItem? = this.find { it.userId == receiver.id }

    val completedAmount: Long
        get() = items.filter { it.used }.sumOf(DistributionItem::amount)

    val isClosed: Boolean
        get() = LocalDateTime.now().isAfter(expiredAt)

    fun receiveAny(receiver: User): DistributionItem? {
        // 만료된 건은 받을 수 없다.
        if (isClosed) throw ExpiredDistributionException(this)
        // 이미 받은 적 있는 사람은 받을 수 없다.
        val receivedRecord = items.findReceiveRecord(receiver)
        if (receivedRecord != null) throw AlreadyReceivedException(receivedRecord)
        // 같은 채팅룸에 속한 사람만 받을 수 있다.
        if (!chatRoom.checkJoined(receiver)) throw NotJoinedUser(receiver)

        return items.find { !it.used }
            ?.apply { receive(receiver) }
            ?.also { log.info("뿌리기 받ㅇ음") }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this.javaClass)

        fun make(creator: User, amount: Long, distributeCnt: Int, chatRoom: ChatRoom): MoneyDistribution {
            val perAmount = amount / distributeCnt
            val remain = amount % distributeCnt

            return MoneyDistribution(creator, chatRoom, amount).apply {
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