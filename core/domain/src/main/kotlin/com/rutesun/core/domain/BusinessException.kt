package com.rutesun.core.domain

class ExpiredDistributionException(val distribution: MoneyDistribution, override val message: String = "이미 만료된 뿌리기 입니다.") : RuntimeException()
class AlreadyReceivedException(val item: DistributionItem, override val message: String = "이미 받은적 있는 뿌리기 입니다.") : RuntimeException()
class NotJoinedUser(val user: User, override val message: String = "받으실 수 없습니다.") : RuntimeException()
