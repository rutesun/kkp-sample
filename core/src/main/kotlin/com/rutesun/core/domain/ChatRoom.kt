package com.rutesun.core.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class ChatRoom(val title: String, users: List<User>) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToMany
    val users: List<User> = users

    fun checkJoined(user: User): Boolean = users.find { it.id == user.id } != null
}