package com.rutesun.core.repository

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface MoneyDistributionRepository : JpaRepository<MoneyDistribution, Long> {
    fun existsByToken(token: Token): Boolean
    fun findByToken(token: Token): MoneyDistribution?

    @EntityGraph(value = "items", type = EntityGraph.EntityGraphType.LOAD)
    fun findWithItemsByToken(token: Token): MoneyDistribution?
}
