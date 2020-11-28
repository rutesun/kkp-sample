package com.rutesun.core.service

import com.rutesun.core.domain.ChatRoom
import com.rutesun.core.domain.ChatRoomRepository
import com.rutesun.core.domain.NotJoinedUser
import com.rutesun.core.domain.NotOwnerException
import com.rutesun.core.domain.Token
import com.rutesun.core.domain.User
import com.rutesun.core.exception.NotFoundException
import com.rutesun.core.exception.NotFoundUserException
import com.rutesun.core.repository.MoneyDistributionRepository
import com.rutesun.core.repository.UserRepository
import com.rutesun.core.util.findByIdOrThrow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
@RunWith(SpringRunner::class)
@Sql(scripts = ["/prepare.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class DistributionServiceImplTest {
    @Autowired
    private lateinit var distributionCreateService: MoneyDistributionCreateService

    @Autowired
    private lateinit var moneyDistributionQueryService: MoneyDistributionQueryService

    @Autowired
    private lateinit var receiveService: ReceiveService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var chatRoomRepository: ChatRoomRepository

    @Autowired
    private lateinit var distributionRepository: MoneyDistributionRepository

    private lateinit var users: List<User>
    private lateinit var chatRooms: List<ChatRoom>
    private lateinit var chatRoom: ChatRoom
    private lateinit var user: User

    private val amount = 10_000L
    private val distributionCnt = 4

    @Before
    fun setup() {
        users = userRepository.findAll()
        user = userRepository.findByIdOrThrow(1)
        chatRoom = chatRoomRepository.findByIdOrThrow(1)

        chatRooms = user.joinedChatRooms!!.map { it.chatRoom }
    }

    private fun testMake(): Token {

        return distributionCreateService.create(user.id, chatRoom.id, amount, distributionCnt)
    }

    @Test
    fun `생성`() {
        println(testMake())
    }

    @Test
    fun `생성 - 실패`() {
        val user = users.first()
        assertFailsWith(NotFoundException::class) {
            distributionCreateService.create(user.id, -999, amount, distributionCnt)
        }
    }

    @Test
    fun `조회`() {
        val token = testMake()
        moneyDistributionQueryService.get(userId = user.id, token = token)
    }

    @Test
    fun `조회 - 실패`() {
        val token = testMake()
        val distribution = distributionRepository.findByToken(token)!!

        assertFailsWith(NotOwnerException::class) {
            moneyDistributionQueryService.get(userId = -999, token = token)
        }
    }

    @Test
    fun `받기`() {
        val token = testMake()
        val lastUser = chatRoom.joinedUsers.last()

        assertTrue(receiveService.receive(lastUser.id, token).amount > 0)
    }

    @Test
    fun `받기 - 실패`() {
        val token = testMake()

        assertFailsWith(NotFoundUserException::class) {
            receiveService.receive(-999, token)
        }

        assertFailsWith(NotJoinedUser::class) {
            receiveService.receive(10, token)
        }
    }
}