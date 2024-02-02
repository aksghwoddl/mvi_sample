package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.data.repository.UserRepository
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

private const val TAG = "DeleteUserUseCaseTest"

class DeleteUserUseCaseTest : BaseTest() {

    @MockK
    private lateinit var userRepository: FakeUserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase
    private lateinit var addUserListUserCase: AddUserUseCase

    override fun setup() {
        super.setup()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository)
        addUserListUserCase = AddUserUseCase(userRepository)
    }

    @Test
    fun `user 삭제 후 정상적으로 삭제되었는지 테스트`() = runTest {
        coEvery {
            var list = emptyList<User>()
            val user: User?
            getUserListUseCase().collect {
                list = it
            }
            if (list.isNotEmpty()) {
                user = list[0]
                deleteUserUseCase(user)

                getUserListUseCase().collect {
                    list = it
                }
                return@coEvery !list.contains(user)
            } else {
                user = User(null, "test", 77)
                addUserListUserCase(user)
                deleteUserUseCase(user)
                return@coEvery !list.contains(user)
            }
        } returns true
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
