package com.example.mvisampleapp.feature.list

import app.cash.turbine.turbineScope
import com.example.domain.usecase.DeleteAllUserUseCase
import com.example.domain.usecase.DeleteUserUseCase
import com.example.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.feature.list.model.ListScreenElements
import com.example.mvisampleapp.feature.list.viewmodel.ListViewModel
import com.example.presenter.feature.list.model.User
import com.example.test.base.BaseTest
import com.example.test.utils.shouldBe
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ListViewModelTest : BaseTest() {
    @MockK
    lateinit var deleteUserUseCase: DeleteUserUseCase

    @MockK
    lateinit var getUserListUseCase: GetUserListUseCase

    @MockK
    lateinit var deleteAllUserUseCase: DeleteAllUserUseCase

    private lateinit var viewModel: ListViewModel

    override fun setup() {
        super.setup()
        viewModel = ListViewModel(
            deleteUserUseCase = deleteUserUseCase,
            getUserListUseCase = getUserListUseCase,
            deleteAllUserUseCase = deleteAllUserUseCase,
        )
    }

    @Test
    fun `OnDeleteButtonClick Event Test`() = runTest {
        turbineScope {
            val state = viewModel.state.testIn(this)

            val user = User(
                name = "테스트",
                age = 7
            )

            state.skipItems(1)

            viewModel.handleEvent(
                ListScreenElements.ListScreenEvent.OnUpdateUserList(
                    userList = listOf(user)
                )
            )

            state.awaitItem().userList.size shouldBe 1

            viewModel.handleEvent(
                ListScreenElements.ListScreenEvent.OnClickUserItem(
                    user = user
                )
            )
            state.awaitItem().selectedUser?.name shouldBe "테스트"

            viewModel.handleEvent(
                ListScreenElements.ListScreenEvent.OnClickDeleteButton(
                    user = user
                )
            )

            state.awaitItem().userList.size shouldBe 0
            state.awaitItem().selectedUser shouldBe null
            state.cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }
}