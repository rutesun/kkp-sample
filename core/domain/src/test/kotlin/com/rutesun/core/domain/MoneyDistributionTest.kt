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
        val users = (1..5).map { User("user$it").also { em.persist(it) } }
        chatRoom = ChatRoom("test room", creator)
        chatRoom.addUsers(users)
        em.persist(chatRoom)
    }

    private val amount = 10_000L
    private val distributionCnt = 4

    @Test
    @Order(1)
    fun `01-뿌리기 만들기`() {
        val distribution = MoneyDistribution.make("token", creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
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
        val distribution = MoneyDistribution.make("token", creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
            .also { testEm.persistAndFlush(it) }

        val items = distribution.items
        distribution.receiveAny(chatRoom.users.toList()[0])
        distribution.receiveAny(chatRoom.users.toList()[1])

        testEm.persistAndFlush(distribution)
        testEm.refresh(distribution)

        assertEquals(amount / 2, distribution.completedAmount)
    }

    @Test
    @Order(3)
    fun `03-받기 실패`() {
        val distribution = MoneyDistribution.make("token", creator, amount = amount, distributeCnt = distributionCnt, chatRoom = chatRoom)
            .also { testEm.persistAndFlush(it) }

        assertFailsWith(NotJoinedUser::class) {
            val other = User("user1").also { em.persist(it) }
            distribution.receiveAny(other)
        }

        assertFailsWith(AlreadyReceivedException::class) {
            val user1 = chatRoom.users.toList()[0]
            distribution.receiveAny(user1)
            distribution.receiveAny(user1)
        }

        assertFailsWith(ExpiredDistributionException::class) {
            ReflectionTestUtils.setField(distribution, "expiredAt", LocalDateTime.now().minusNanos(1L))

            val user2 = chatRoom.users.toList()[1]
            distribution.receiveAny(user2)
        }
    }
}