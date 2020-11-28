package com.rutesun.app.web

import com.rutesun.core.domain.AlreadyReceivedException
import com.rutesun.core.domain.ExpiredDistributionException
import com.rutesun.core.domain.NotJoinedUser
import com.rutesun.core.domain.NotOwnerException
import com.rutesun.core.exception.MoneyExhaustedException
import com.rutesun.core.exception.NotFoundException
import com.rutesun.core.exception.NotFoundUserException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionAdvice {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(value = [RuntimeException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleDefault(ex: RuntimeException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(value = [ExpiredDistributionException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleExpiredDistributionException(ex: ExpiredDistributionException): ResponseEntity<*> {
        val message = "${ex.distribution.id}, ${ex.message}"
        log.error(message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(value = [AlreadyReceivedException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAlreadyReceivedException(ex: AlreadyReceivedException): ResponseEntity<*> {
        val message = "(유저: ${ex.item.receiverId}, 받은 시간: ${ex.item.updatedAt}), ${ex.message}"
        log.error(message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message)
    }

    @ExceptionHandler(value = [NotJoinedUser::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleNotJoinedUser(ex: NotJoinedUser): ResponseEntity<*> {
        val message = "${ex.user.id}, ${ex.message}"
        log.error(message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message)
    }

    @ExceptionHandler(value = [NotOwnerException::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleNotOwnerException(ex: NotOwnerException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<*> {
        log.error(ex.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(value = [NotFoundUserException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundUserException(ex: NotFoundUserException): ResponseEntity<*> {
        val message = "${ex.userId}, ${ex.message}"
        log.error(message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(value = [MoneyExhaustedException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleMoneyExhaustedException(ex: MoneyExhaustedException): ResponseEntity<*> {
        val message = "${ex.moneyDistribution.id}, ${ex.message}"
        log.error(message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }
}
