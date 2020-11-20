package com.rutesun.core.domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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

    @OneToMany(mappedBy = "distribution")
    var items: List<DistributionItem> = emptyList()
        private set

    @ManyToOne
    @JoinColumn(name = "creator_id")
    val creator: User = creator

    val totalAmount = totalAmount

    private val expiredAt: LocalDateTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES)

    private fun List<DistributionItem>.hasReceiveRecord(receiver: User): Boolean = this.any { it.userId == receiver.id }

    val completedAmount: Long
        get() = items.filter { it.used }.sumOf(DistributionItem::amount)

    val isClosed: Boolean
        get() = LocalDateTime.now().isAfter(expiredAt)

    fun receiveAny(receiver: User): DistributionItem? {
        // 만료된 건은 받을 수 없다.
        if (isClosed) throw RuntimeException()
        // 이미 받은 적 있는 사람은 받을 수 없다.
        if (items.hasReceiveRecord(receiver)) throw RuntimeException()
        // 같은 채팅룸에 속한 사람만 받을 수 있다.
        if (!chatRoom.checkJoined(receiver)) throw RuntimeException()

        return items.find { !it.used }?.apply { receive(receiver) }
    }

    companion object {
        fun make(creator: User, amount: Long, distributeCnt: Int, chatRoom: ChatRoom): MoneyDistribution {
            val perAmount = amount / distributeCnt
            val remain = amount % distributeCnt

            return MoneyDistribution(creator, chatRoom, amount).apply {
                val items = ArrayList<DistributionItem>(distributeCnt)
                for (i in 0..distributeCnt) {
                    items[i] = if (i == 0) DistributionItem(this, perAmount + remain)
                    else DistributionItem(this, perAmount)
                }
                this.items = items
            }
        }
    }
}