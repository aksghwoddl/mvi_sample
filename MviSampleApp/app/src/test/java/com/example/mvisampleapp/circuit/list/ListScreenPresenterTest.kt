package com.example.mvisampleapp.circuit.list

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.domain.model.UserModel
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListFlowUseCase
import com.example.mvisampleapp.ui.circuit.list.presenter.ListScreenPresenter
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.ui.model.User
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
    lateinit var getUserListFlowUseCase: GetUserListFlowUseCase

    @Test
    fun `OnClickDeleteUserList Event Test`() = runTest {
        val userModel = UserModel(
            age = 7,
            name = "테스트"
        )

        coEvery {
            getUserListFlowUseCase()
        } returns flow {
            emit(
                listOf(userModel)
            )
        }

        coEvery {
            deleteUserUseCase(
                name = any(),
                age = any(),
            )
        } returns Unit

        ListScreenPresenter(
            navigator = FakeNavigator(ListScreen),
            deleteUserUseCase = deleteUserUseCase,
            getUserListFlowUseCase = getUserListFlowUseCase
        ).test {
            awaitItem().eventSink(
                ListScreen.State.ListScreenEvent.OnClickUserItem(
                    user = User(
                        age = 7,
                        name = "테스트"
                    )
                )
            )
            awaitItem().eventSink(
                ListScreen.State.ListScreenEvent.OnClickDeleteButton(
                    age = 7,
                    name = "테스트"
                )
            )

            testScheduler.advanceUntilIdle()

            expectMostRecentItem().listModel.selectedUser shouldBe null
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickPreviousButton Event Test`() = runTest {
        val navigator = FakeNavigator(ListScreen)
        ListScreenPresenter(
            navigator = navigator,
            deleteUserUseCase = deleteUserUseCase,
            getUserListFlowUseCase = getUserListFlowUseCase
        ).test {
            awaitItem().eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
            navigator.awaitNextScreen() shouldBe MainScreen
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }
}