package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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
        val userEntity = UserEntity(null, "test", 77)
        coEvery {
            userRepository.getAllUser()
        } returns flow {
            emit(listOf(userEntity))
        }
        val ret = withContext(Dispatchers.Main) {
            getUserListUseCase().collect { list ->
                if (list.isNotEmpty()) {
                    coEvery {
                        userRepository.getAllUser()
                    } returns flow {
                        emit(listOf())
                    }
                    deleteUserUseCase(
                        name = list.first().name,
                        age = list.first().age
                    )
                }
            }
            getUserListUseCase().first().size
        }
        ret shouldBe 0
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
