package com.rutesun.core.service

import com.rutesun.core.domain.DistributionItem
import com.rutesun.core.domain.NotJoinedUser
import com.rutesun.core.domain.Token
import org.springframework.stereotype.Service

interface ReceiveService {
    fun receive(receiverId: Long, token: Token): DistributionItem
}

@Service
class ReceiveServiceImpl(
    private val chatRoomService: ChatRoomService,
    private val userRepository: UserRepository,
    private val distributionRepository: MoneyDistributionRepository
) : ReceiveService {

    override fun receive(receiverId: Long, token: Token): DistributionItem {
        val receiver = userRepository.findByIdOrThrow(receiverId)
        val moneyDistribution = distributionRepository.findByTokenOrThrow(token)

        if (!chatRoomService.checkJoined(receiverId, moneyDistribution.chatRoom.id)) throw NotJoinedUser(receiver)

        if (moneyDistribution.isEmpty) throw MoneyExhaustedException(moneyDistribution)

        val item = moneyDistribution.receiveAny(receiver) ?: throw MoneyExhaustedException(moneyDistribution)
        distributionRepository.save(moneyDistribution)
        return item
    }
}