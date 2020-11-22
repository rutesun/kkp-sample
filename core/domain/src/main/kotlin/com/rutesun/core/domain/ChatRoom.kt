package com.rutesun.core.domain

import java.util.LinkedList
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class ChatRoom(val title: String, creator: User) : WithUpdatedTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val users: List<User> = LinkedList<User>().apply { add(creator) }

    fun checkJoined(user: User): Boolean = users.find { it.id == user.id } != null

    fun addUser(user: User) {
        val users = this.users as LinkedList
        users.add(user.join(this))
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