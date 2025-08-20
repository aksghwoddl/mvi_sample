package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddUserUseCaseTest : BaseTest() {

    @MockK
    private lateinit var userRepository: FakeUserRepository
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        addUserUseCase = AddUserUseCase(userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository)
    }

    @Test
    fun `user 저장 후 정상적으로 값이 저장 되었는지 테스트`() = runTest {
        val userEntity = UserEntity(null, "test1", 77)

        coEvery {
            userRepository.getAllUser()
        } returns flow {
            emit(listOf(userEntity))
        }

        addUserUseCase(
            name = userEntity.name,
            age = userEntity.age
        )
        getUserListUseCase().collect {
            it.size shouldBe 1
            it.first().name shouldBe "test1"
            it.first().age shouldBe 77
        }
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
