package com.rutesun.core.service

import com.rutesun.core.domain.ChatRoomRepository
import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.NotOwnerException
import com.rutesun.core.domain.Token
import com.rutesun.core.repository.MoneyDistributionRepository
import com.rutesun.core.repository.UserRepository
import com.rutesun.core.util.findByIdOrThrow
import com.rutesun.core.util.findByTokenOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

interface DistributionService {
    fun make(creatorId: Long, chatRoomId: Long, amount: Long, distributionCnt: Int): Token
    fun get(userId: Long, token: Token): MoneyDistribution?
}

@Service
class DistributionServiceImpl(
    private val tokenGenerator: TokenGenerator,
    private val userRepository: UserRepository,
    private val distributionRepository: MoneyDistributionRepository,
    private val chatRoomRepository: ChatRoomRepository
) : DistributionService {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun make(creatorId: Long, chatRoomId: Long, amount: Long, distributionCnt: Int): Token {
        val creator = userRepository.findByIdOrThrow(creatorId)
        val chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId)

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