package com.rutesun.app.web

import com.rutesun.core.domain.Token
import com.rutesun.core.exception.NotFoundException
import com.rutesun.core.service.MoneyDistributionCreateService
import com.rutesun.core.service.MoneyDistributionQueryService
import com.rutesun.core.service.ReceiveService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/distribution")
class MoneyDistributionController(
    private val moneyDistributionQueryService: MoneyDistributionQueryService,
    private val moneyDistributionCreateService: MoneyDistributionCreateService,
    private val receiveService: ReceiveService
) {
    @PostMapping
    fun create(@RequestHeader(USER_KEY) creatorKey: Long, @RequestHeader(ROOM_KEY) chatRoomKey: String, @RequestParam amount: Long, @RequestParam distributionCount: Int): ResponseEntity<Token> {
        val token = moneyDistributionCreateService.create(creatorKey, chatRoomKey.toLong(), amount, distributionCount)
        return ResponseEntity.ok(token)
    }

    @GetMapping("/{token}")
    fun getInfo(@RequestHeader(USER_KEY) userKey: Long, @PathVariable token: String): ResponseEntity<DistributionResultDto> {
        val distribution = moneyDistributionQueryService.get(userKey, token) ?: throw NotFoundException()
        return ResponseEntity.ok(DistributionResultDto.of(distribution))
    }

    @GetMapping("/{token}/money")
    fun getMoney(@RequestHeader(USER_KEY) receiverKey: Long, @PathVariable token: String): ResponseEntity<Long> {
        val item = receiveService.receive(receiverKey, token)
        return ResponseEntity.ok(item.amount)
    }
}