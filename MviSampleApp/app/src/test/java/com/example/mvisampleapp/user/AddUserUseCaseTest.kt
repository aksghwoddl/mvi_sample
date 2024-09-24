package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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
        val user = User(null, "test1", 77)
        var list: List<User> = emptyList()

        coEvery {
            userRepository.getAllUser()
        } returns flow {
            emit(listOf(user))
        }

        val ret = withContext(Dispatchers.Main) {
            addUserUseCase(user)
            getUserListUseCase().collect {
                list = it
            }
            list.isNotEmpty() && list.contains(user)
        }
        ret shouldBe true
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
