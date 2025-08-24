package com.example.domain.user

import app.cash.turbine.test
import com.example.data.db.entity.UserEntity
import com.example.data.repository.UserRepository
import com.example.domain.usecase.AddUserUseCase
import com.example.domain.usecase.GetUserListFlowUseCase
import com.example.test.base.BaseTest
import com.example.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetUserListFlowUseCaseTest : BaseTest() {
    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var getUserListFlowUseCase: GetUserListFlowUseCase
    private lateinit var addUserUseCase: AddUserUseCase

    override fun setup() {
        super.setup()
        getUserListFlowUseCase = GetUserListFlowUseCase(userRepository = userRepository)
        addUserUseCase = AddUserUseCase(userRepository = userRepository)
    }

    @Test
    fun `Flow 정상 동작 확인 테스트`() = runTest {
        val userEntity = UserEntity(null, "test", 77)

        val ret = mutableListOf<UserEntity>()

        coEvery { userRepository.getAllUser() } returns flow { emit(ret) }

        coEvery { userRepository.addUser(userEntity = userEntity) } coAnswers {
            ret.add(userEntity)
        }

        addUserUseCase(name = userEntity.name , age = userEntity.age)

        getUserListFlowUseCase().collect {
            it.size shouldBe 1
            it.first().age shouldBe 77
            it.first().name shouldBe "test"
        }
    }
}