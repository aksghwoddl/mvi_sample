package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.repository.UserRepository
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.DeleteAllUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteAllUserUseCaseTest : BaseTest() {
    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var deleteAllUserUseCase: DeleteAllUserUseCase

    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        addUserUseCase = AddUserUseCase(userRepository = userRepository)
        deleteAllUserUseCase = DeleteAllUserUseCase(userRepository = userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository = userRepository)
    }

    @Test
    fun `전체 삭제 UseCase 테스트`() = runTest {
        addUserUseCase(
            name = "test1",
            age = 1
        )

        addUserUseCase(
            name = "test2",
            age = 2
        )

        deleteAllUserUseCase()

        getUserListUseCase().collect {
            it.size shouldBe 0
        }
    }
}