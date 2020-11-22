package com.ruetrunn.core.service

import com.rutesun.core.domain.DistributionItem
import com.rutesun.core.domain.Token
import org.springframework.stereotype.Service

interface ReceiveService {
    fun receive(receiverId: Long, token: Token): DistributionItem
}

@Service
class ReceiveServiceImpl(
    private val distributionService: DistributionService,
    private val userRepository: UserRepository,
    private val distributionRepository: MoneyDistributionRepository
) : ReceiveService {

    override fun receive(receiverId: Long, token: Token): DistributionItem {
        val receiver = userRepository.findByIdOrThrow(receiverId)
        val moneyDistribution = distributionService.get(token)
        if (moneyDistribution.isEmpty) throw MoneyExhaustedException(moneyDistribution)

        val item = moneyDistribution.receiveAny(receiver) ?: throw MoneyExhaustedException(moneyDistribution)
        distributionRepository.save(moneyDistribution)
        return item
    }
}