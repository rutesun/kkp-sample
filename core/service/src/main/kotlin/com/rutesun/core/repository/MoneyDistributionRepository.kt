package com.rutesun.core.repository

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MoneyDistributionRepository : JpaRepository<MoneyDistribution, Long> {
    fun existsByToken(token: Token): Boolean
    fun findByToken(token: Token): MoneyDistribution?

    @Query("SELECT md FROM MoneyDistribution md INNER JOIN FETCH md.items WHERE md.token = ?1")
    fun findWithItemsByToken(token: Token): MoneyDistribution?
}
