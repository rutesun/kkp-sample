package com.rutesun.core.service

import com.rutesun.core.domain.ChatRoomRepository
import org.springframework.stereotype.Service

interface ChatRoomService {
    fun checkJoined(userId: Long, roomId: Long): Boolean
}

@Service
class ChatRoomServiceImpl(
    private val chatRoomRepository: ChatRoomRepository
) : ChatRoomService {
    override fun checkJoined(userId: Long, roomId: Long): Boolean {
        val rooms = chatRoomRepository.findAllByUserIdAndRoomId(userId, roomId)
        return rooms != null
    }
}