package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddUserUseCaseTest : BaseTest() {
    private lateinit var userRepository: FakeUserRepository
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        userRepository = FakeUserRepository()
        addUserUseCase = AddUserUseCase(userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository)
    }

    @Test
    fun `user 저장 후 정상적으로 값이 저장 되었는지 테스트`() = runTest {
        addUserUseCase(
            name = "test1",
            age = 77
        )

        with(getUserListUseCase()) {
            size shouldBe 1
            first().name shouldBe "test1"
            first().age shouldBe 77
        }
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
