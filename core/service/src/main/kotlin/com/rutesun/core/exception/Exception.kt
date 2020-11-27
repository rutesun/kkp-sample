package com.rutesun.core.exception

import com.rutesun.core.domain.MoneyDistribution

open class NotFoundException(override val message: String = "리소스를 찾을 수 없습니다.") : RuntimeException()
data class NotFoundUserException(val userId: Long) : NotFoundException("유저를 찾을 수 없습니다.")
data class MoneyExhaustedException(val moneyDistribution: MoneyDistribution, override val message: String = "받을 수 있는 돈이 없습니다.") : RuntimeException()
