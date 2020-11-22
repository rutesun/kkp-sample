package com.rutesun.core.service

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token
import org.springframework.data.jpa.repository.JpaRepository

interface MoneyDistributionRepository : JpaRepository<MoneyDistribution, Long> {
    fun existsByToken(token: Token): Boolean
    fun findByToken(token: Token): MoneyDistribution?
}
