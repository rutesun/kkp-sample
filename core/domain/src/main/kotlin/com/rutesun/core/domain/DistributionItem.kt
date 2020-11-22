package com.rutesun.core.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class DistributionItem(distribution: MoneyDistribution, amount: Long) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var used = false
        private set
    var userId: Long? = null
        private set
    val amount: Long = amount

    @Column(name = "distribution_id", insertable = false, updatable = false)
    private val distributionId: Long = distribution.id

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    val distribution: MoneyDistribution = distribution

    internal fun receive(receiver: User) {
        this.used = true
        this.userId = receiver.id
    }
}