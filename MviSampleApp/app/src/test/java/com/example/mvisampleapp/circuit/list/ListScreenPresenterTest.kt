package com.example.mvisampleapp.circuit.list

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.circuit.list.presenter.ListScreenPresenter
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.utils.shouldBe
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ListScreenPresenterTest : BaseTest() {
    @MockK
    lateinit var deleteUserUseCase: DeleteUserUseCase

    @MockK
    lateinit var getUserListUseCase: GetUserListUseCase

    @Test
    fun `OnClickDeleteUserList Event Test`() = runTest {
        val user = User(
            age = 7,
            name = "테스트"
        )

        coEvery {
            getUserListUseCase()
        } returns flow {
            emit(listOf(user))
        }

        ListScreenPresenter(
            navigator = FakeNavigator(ListScreen),
            deleteUserUseCase = deleteUserUseCase,
            getUserListUseCase = getUserListUseCase
        ).test {
            awaitItem().eventSink(ListScreen.State.ListScreenEvent.OnUpdateUserList)
            skipItems(2)
            awaitItem().eventSink(ListScreen.State.ListScreenEvent.OnClickUserItem(user = user))
            awaitItem().eventSink(ListScreen.State.ListScreenEvent.OnClickDeleteButton(user = user))
            skipItems(4)
            awaitItem().listModel.selectedUser shouldBe null
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickPreviousButton Event Test`() = runTest {
        val navigator = FakeNavigator(ListScreen)
        ListScreenPresenter(
            navigator = navigator,
            deleteUserUseCase = deleteUserUseCase,
            getUserListUseCase = getUserListUseCase
        ).test {
            skipItems(1)
            awaitItem().eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
            navigator.awaitNextScreen() shouldBe MainScreen
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }
}