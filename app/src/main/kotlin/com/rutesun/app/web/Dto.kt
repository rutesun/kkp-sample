package com.rutesun.app.web

import com.rutesun.core.domain.MoneyDistribution
import java.time.LocalDateTime

data class DistributionResultDto(
    val totalAmount: Long,
    val completedAmount: Long,
    val createdAt: LocalDateTime
) {
    var items: List<Item> = emptyList()
        private set

    data class Item(val receiveAmount: Long, val receiverUserId: Long?)

    companion object {
        fun of(distribution: MoneyDistribution): DistributionResultDto {
            val dto = DistributionResultDto(distribution.totalAmount, distribution.completedAmount, distribution.createdAt)
            dto.items = distribution.items.map { Item(it.amount, it.receiverId) }
            return dto
        }
    }
}