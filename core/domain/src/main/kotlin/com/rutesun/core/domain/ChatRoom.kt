package com.rutesun.core.domain

import java.util.LinkedList
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class ChatRoom(val title: String, creator: User) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE])
    val joinedUsers: List<UserChatRoom> = LinkedList<UserChatRoom>()

    fun checkJoined(user: User): Boolean = joinedUsers.find { it.user.id == user.id } != null

    fun addUser(user: User) {
        val joined = this.joinedUsers as LinkedList
        joined.add(UserChatRoom(user, this))
    }

    fun addUsers(vararg users: User) {
        for (u in users) {
            addUser(u)
        }
    }

    fun addUsers(users: Collection<User>) {
        for (u in users) {
            addUser(u)
        }
    }
}