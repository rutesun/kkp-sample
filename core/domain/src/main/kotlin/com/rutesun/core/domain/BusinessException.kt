package com.rutesun.core.domain

class ExpiredDistributionException(val distribution: MoneyDistribution, override val message: String = "이미 만료된 뿌리기 입니다.") : RuntimeException()
class AlreadyReceivedException(val item: DistributionItem, override val message: String = "이미 받은적 있는 뿌리기 입니다.") : RuntimeException()
class NotJoinedUser(val user: User, override val message: String = "채팅방에 속하지 않으셔서 받으실 수 없습니다.") : RuntimeException()

class NotOwnerException() : RuntimeException("조회할 수 있는 권한이 없습니다.")