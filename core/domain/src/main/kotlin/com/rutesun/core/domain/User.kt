package com.rutesun.core.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class User(name: String) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    val name: String = name

    @ManyToOne
    @JoinColumn
    var chatRoom: ChatRoom? = null

    fun join(chatRoom: ChatRoom): User = this.apply { this.chatRoom = chatRoom }
}