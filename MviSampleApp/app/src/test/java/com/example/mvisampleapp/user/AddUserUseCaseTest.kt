package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.data.repository.UserRepository
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddUserUseCaseTest : BaseTest() {

    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        addUserUseCase = AddUserUseCase(userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository)
    }

    @Test
    fun `user 저장 후 정상적으로 값이 저장 되었는지 테스트`() = runTest {
        val user = User(null, "test1", 77)
        addUserUseCase(user)

        coEvery {
            var list: List<User> = emptyList()
            getUserListUseCase().collect {
                list = it
            }
            return@coEvery list.isNotEmpty() && list.contains(user)
        } returns true
    }

    override fun tearDown() {
        super.tearDown()
    }
}
