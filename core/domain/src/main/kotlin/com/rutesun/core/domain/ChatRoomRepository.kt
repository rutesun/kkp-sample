package com.rutesun.core.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    @Query("SELECT r FROM UserChatRoom ucr JOIN ucr.user u  JOIN ucr.chatRoom r WHERE ucr.user.id = ?1 AND ucr.chatRoom.id = ?2")
    fun findAllByUserIdAndRoomId(userId: Long, roomId: Long): ChatRoom?
}