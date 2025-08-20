package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.domain.model.UserModel
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListFlowUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetUserListFlowUseCaseTest : BaseTest() {
    private val userRepository = FakeUserRepository()
    private lateinit var getUserListFlowUseCase: GetUserListFlowUseCase
    private lateinit var addUserUseCase: AddUserUseCase

    override fun setup() {
        super.setup()
        getUserListFlowUseCase = GetUserListFlowUseCase(userRepository = userRepository)
        addUserUseCase = AddUserUseCase(userRepository = userRepository)
    }

    @Test
    fun `Flow 정상 동작 확인 테스트`() = runTest {
        addUserUseCase(
            name = "이름",
            age = 1234
        )

        getUserListFlowUseCase().first() shouldBe listOf(
            UserModel(
                name = "이름",
                age = 1234
            )
        )
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}