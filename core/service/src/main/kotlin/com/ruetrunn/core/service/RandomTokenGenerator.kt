package com.ruetrunn.core.service

import com.rutesun.core.domain.Token
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class RandomTokenGenerator(
    private val moneyDistributionRepository: MoneyDistributionRepository
) : TokenGenerator {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun generate(): Token {
        var randomToken = randomAlphabetToken(size = 3)
        var exists: Boolean
        // 경우의 수를 벗어난 토큰 생성은 없을 거라 가정한다.
        do {
            exists = moneyDistributionRepository.existsByToken(randomToken)
        } while (exists)
        return randomToken
    }

    private fun randomAlphabetToken(size: Int) = RandomStringUtils.randomAlphabetic(size)
}