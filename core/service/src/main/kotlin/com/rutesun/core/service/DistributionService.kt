package com.rutesun.core.service

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.NotOwnerException
import com.rutesun.core.domain.Token
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

interface DistributionService {
    fun make(creatorId: Long, amount: Long, distributionCnt: Int): Token
    fun get(userId: Long, token: Token): MoneyDistribution?
}

@Service
class DistributionServiceImpl(
    private val tokenGenerator: TokenGenerator,
    private val userRepository: UserRepository,
    private val distributionRepository: MoneyDistributionRepository
) : DistributionService {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun make(creatorId: Long, amount: Long, distributionCnt: Int): Token {
        val creator = userRepository.findByIdOrThrow(creatorId)
        val chatRoom = creator.chatRoom ?: throw NotFoundException("유저가 속한 채팅방이 없습니다.")
        val token = tokenGenerator.generate()
        val distribution = MoneyDistribution.make(token, creator = creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
        distributionRepository.save(distribution)
        return token
    }

    override fun get(userId: Long, token: Token): MoneyDistribution? {
        val distribution = distributionRepository.findByTokenOrThrow(token)
        if (distribution.creator.id != userId) throw NotOwnerException()
        if (distribution.isExpired) return null
        return distribution
    }
}