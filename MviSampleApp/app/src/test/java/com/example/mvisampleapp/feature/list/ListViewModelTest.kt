package com.example.mvisampleapp.feature.list

import app.cash.turbine.testIn
import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.feature.list.model.ListScreenElements
import com.example.mvisampleapp.ui.feature.list.viewmodel.ListViewModel
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ListViewModelTest : BaseTest() {
    @MockK
    lateinit var deleteUserUseCase: DeleteUserUseCase

    @MockK
    lateinit var getUserListUseCase: GetUserListUseCase

    private lateinit var viewModel: ListViewModel

    override fun setup() {
        super.setup()
        viewModel = ListViewModel(
            deleteUserUseCase = deleteUserUseCase,
            getUserListUseCase = getUserListUseCase
        )
    }

    @Test
    fun `OnDeleteButtonClick Event Test`() = runTest {
        val state = viewModel.state.testIn(this)
        val user = User(
            index = 1,
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

        state.awaitItem().selectedUser shouldBe null
        state.cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
    }
}