package com.ruetrunn.core.service

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token
import com.rutesun.core.domain.User
import org.springframework.data.repository.findByIdOrNull

fun UserRepository.findByIdOrThrow(creatorId: Long): User = findByIdOrNull(creatorId) ?: throw NotFoundUserException(creatorId)
fun MoneyDistributionRepository.findByTokenOrThrow(token: Token): MoneyDistribution = findByToken(token)
    ?: throw NotFoundException("$token 에 해당하는 뿌리기 기록이 없습니다.")