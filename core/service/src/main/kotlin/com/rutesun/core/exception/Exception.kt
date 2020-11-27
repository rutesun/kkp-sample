package com.rutesun.core.exception

import com.rutesun.core.domain.MoneyDistribution
import com.rutesun.core.domain.Token

open class NotFoundException(override val message: String = "리소스를 찾을 수 없습니다.") : RuntimeException() {
    companion object {
        fun ofToken(token: Token): NotFoundException = NotFoundException("$token 에 해당하는 뿌리기 기록이 없습니다.")

        fun ofRoom(roomId: Long): NotFoundException = NotFoundException("$roomId 에 해당하는 채팅방 기록이 없습니다.")
    }
}

data class NotFoundUserException(val userId: Long) : NotFoundException("유저를 찾을 수 없습니다.")
data class MoneyExhaustedException(val moneyDistribution: MoneyDistribution, override val message: String = "받을 수 있는 돈이 없습니다.") : RuntimeException()
