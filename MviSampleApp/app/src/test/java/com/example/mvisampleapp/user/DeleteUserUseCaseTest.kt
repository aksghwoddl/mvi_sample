package com.example.mvisampleapp.user

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.domain.model.UserModel
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.repository.FakeUserRepository
import com.example.mvisampleapp.utils.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

private const val TAG = "DeleteUserUseCaseTest"

class DeleteUserUseCaseTest : BaseTest() {
    private lateinit var userRepository: FakeUserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase
    private lateinit var addUserListUserCase: AddUserUseCase

    override fun setup() {
        super.setup()
        userRepository = FakeUserRepository()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository)
        addUserListUserCase = AddUserUseCase(userRepository)
    }

    @Test
    fun `user 삭제 후 정상적으로 삭제되었는지 테스트`() = runTest {
        val userEntity = UserEntity(null, "test", 77)

        addUserListUserCase(
            name = userEntity.name,
            age = userEntity.age
        )

        getUserListUseCase() shouldBe listOf(
            UserModel(
                name = userEntity.name,
                age = userEntity.age
            )
        )

        deleteUserUseCase(
            name = userEntity.name,
            age = userEntity.age
        )

        getUserListUseCase().size shouldBe 0
    }

    override fun tearDown() {
        super.tearDown()
        userRepository.closeDatabase()
    }
}
