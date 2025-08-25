package com.example.domain.user

import com.example.data.db.entity.UserEntity
import com.example.data.repository.UserRepository
import com.example.domain.usecase.GetUserListUseCase
import com.example.test.base.BaseTest
import com.example.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddUserUseCaseTest : BaseTest() {
    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        getUserListUseCase = GetUserListUseCase(userRepository)
    }

    @Test
    fun `유저목로 가져오기 테스트`() = runTest {
        val user = UserEntity(
            name = "test1",
            age = 77
        )
        coEvery { userRepository.getAllUser() } returns flow {
            emit(listOf(user))
        }

        with(getUserListUseCase()) {
            size shouldBe 1
            first().name shouldBe "test1"
            first().age shouldBe 77
        }
    }
}
