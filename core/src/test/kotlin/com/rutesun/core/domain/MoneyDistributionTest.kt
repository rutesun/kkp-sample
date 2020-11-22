package com.rutesun.core.domain

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(SpringRunner::class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MoneyDistributionTest {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var testEm: TestEntityManager

    private lateinit var creator: User
    private lateinit var chatRoom: ChatRoom

    @Before
    fun setup() {
        creator = User("test").also { em.persist(it) }
    }

    private val amount = 10_000L
    private val distributionCnt = 4

    private fun makeTestUserAndChatRoom(userCnt: Int): ChatRoom {
        val users = (1..userCnt).map { User("user$it").also { u -> em.persist(u) } }
        chatRoom = ChatRoom("test room", listOf(creator, *users.toTypedArray()))
            .also { em.persist(it) }
        return chatRoom
    }

    @Test
    @Order(1)
    fun `01-뿌리기 만들기`() {
        val chatRoom = makeTestUserAndChatRoom(5)

        val distribution = MoneyDistribution.make(creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
        testEm.persistAndFlush(distribution)
        testEm.refresh(distribution)

        val found = testEm.find(MoneyDistribution::class.java, distribution.id)

        assertEquals(distribution.totalAmount, found.totalAmount)
        assertEquals(distributionCnt, distribution.items.size)
        assertEquals(0L, found.completedAmount)
    }

    @Test
    @Order(2)
    fun `02-뿌린 금액 계산`() {
        val chatRoom = makeTestUserAndChatRoom(5)
        val distribution = MoneyDistribution.make(creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
            .also { testEm.persistAndFlush(it) }

        val items = distribution.items
        distribution.receiveAny(chatRoom.users[0])
        distribution.receiveAny(chatRoom.users[1])

        testEm.persistAndFlush(distribution)
        testEm.refresh(distribution)

        assertEquals(amount / 2, distribution.completedAmount)
    }

    @Test
    @Order(3)
    fun `03-받기 실패`() {
        val chatRoom = makeTestUserAndChatRoom(5)
        val distribution = MoneyDistribution.make(creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
            .also { testEm.persistAndFlush(it) }

        assertFailsWith(NotJoinedUser::class) {
            val other = User("user1").also { em.persist(it) }
            distribution.receiveAny(other)
        }

        assertFailsWith(AlreadyReceivedException::class) {
            val user1 = chatRoom.users[0]
            distribution.receiveAny(user1)
            distribution.receiveAny(user1)
        }

        assertFailsWith(ExpiredDistributionException::class) {
            ReflectionTestUtils.setField(distribution, "expiredAt", LocalDateTime.now().minusNanos(1L))

            val user2 = chatRoom.users[1]
            distribution.receiveAny(user2)
        }
    }
}