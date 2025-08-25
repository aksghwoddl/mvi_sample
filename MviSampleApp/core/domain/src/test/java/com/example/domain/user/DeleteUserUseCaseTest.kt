package com.example.domain.user

import com.example.data.db.entity.UserEntity
import com.example.data.repository.UserRepository
import com.example.domain.model.UserModel
import com.example.domain.usecase.AddUserUseCase
import com.example.domain.usecase.DeleteUserUseCase
import com.example.domain.usecase.GetUserListUseCase
import com.example.test.base.BaseTest
import com.example.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

private const val TAG = "DeleteUserUseCaseTest"

class DeleteUserUseCaseTest : BaseTest() {
    @MockK
    lateinit var userRepository: UserRepository
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
        val user = UserEntity(null, "test", 77)

        val ret = mutableListOf<UserEntity>()

        coEvery {
            userRepository.getAllUser()
        } returns flow { emit(ret) }

        coEvery {
            userRepository.addUser(userEntity = user)
        } coAnswers {
            ret.add(user)
        }

        coEvery {
            userRepository.deleteUser(
                name = user.name,
                age = user.age
            )
        } coAnswers {
            ret.remove(user)
        }

        addUserListUserCase(
            name = user.name,
            age = user.age
        )

        getUserListUseCase() shouldBe listOf(
            UserModel(
                name = user.name,
                age = user.age
            )
        )

        deleteUserUseCase(
            name = user.name,
            age = user.age
        )

        getUserListUseCase().size shouldBe 0
    }
}
