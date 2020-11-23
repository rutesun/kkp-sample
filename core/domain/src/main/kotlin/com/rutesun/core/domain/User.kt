package com.rutesun.core.domain

import java.util.LinkedList
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User(name: String) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    val name: String = name

    @OneToMany(mappedBy = "user")
    var joinedChatRooms: List<UserChatRoom>? = LinkedList()
}