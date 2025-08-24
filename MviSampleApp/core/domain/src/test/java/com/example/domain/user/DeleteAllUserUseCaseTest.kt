package com.example.domain.user

import com.example.data.db.entity.UserEntity
import com.example.data.repository.UserRepository
import com.example.domain.usecase.DeleteAllUserUseCase
import com.example.domain.usecase.GetUserListUseCase
import com.example.test.base.BaseTest
import com.example.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteAllUserUseCaseTest : BaseTest() {
    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var deleteAllUserUseCase: DeleteAllUserUseCase
    private lateinit var getUserListUseCase: GetUserListUseCase

    override fun setup() {
        super.setup()
        deleteAllUserUseCase = DeleteAllUserUseCase(userRepository = userRepository)
        getUserListUseCase = GetUserListUseCase(userRepository = userRepository)
    }

    @Test
    fun `전체 삭제 UseCase 테스트`() = runTest {
        var ret = listOf(UserEntity(name = "", age = 77))
        coEvery { userRepository.getAllUser() } returns flow {
            emit(ret)
        }
        coEvery { userRepository.deleteAll() } coAnswers {
            ret = emptyList()
        }

        getUserListUseCase().size shouldBe 1
        deleteAllUserUseCase()
        getUserListUseCase().size shouldBe 0

    }
}