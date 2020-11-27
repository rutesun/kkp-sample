package com.rutesun.core.util

import com.rutesun.core.domain.ChatRoom
import com.rutesun.core.domain.ChatRoomRepository
import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token
import com.rutesun.core.domain.User
import com.rutesun.core.exception.NotFoundException
import com.rutesun.core.exception.NotFoundUserException
import com.rutesun.core.repository.MoneyDistributionRepository
import com.rutesun.core.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull

fun UserRepository.findByIdOrThrow(creatorId: Long): User = findByIdOrNull(creatorId) ?: throw NotFoundUserException(creatorId)

fun MoneyDistributionRepository.findByTokenOrThrow(token: Token): MoneyDistribution = findByToken(token)
    ?: throw NotFoundException("$token 에 해당하는 뿌리기 기록이 없습니다.")

fun ChatRoomRepository.findByIdOrThrow(roomId: Long): ChatRoom = findByIdOrNull(roomId)
    ?: throw NotFoundException("$roomId 에 해당하는 채팅방 기록이 없습니다.")