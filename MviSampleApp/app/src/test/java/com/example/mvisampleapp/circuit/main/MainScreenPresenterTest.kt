package com.example.mvisampleapp.circuit.main

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.presenter.MainScreenPresenter
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.utils.shouldBe
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainScreenPresenterTest : BaseTest() {
    @MockK
    lateinit var addUserUseCase: AddUserUseCase

    @Test
    fun `OnClickAddUserButton Event 성공 Test`() = runTest {
        MainScreenPresenter(
            navigator = FakeNavigator(MainScreen),
            addUserUseCase = addUserUseCase
        ).test {
            coEvery {
                addUserUseCase(
                    name = any(),
                    age = any(),
                )
            } returns Unit

            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age = "9"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserName(name = "테스트"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)

            testScheduler.advanceUntilIdle()

            with(expectMostRecentItem()) {
                mainModel.name shouldBe ""
                mainModel.age shouldBe ""
            }
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickAddUserButton Event 실패 Test`() = runTest {
        MainScreenPresenter(
            navigator = FakeNavigator(MainScreen),
            addUserUseCase = addUserUseCase
        ).test {
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age = "9"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)

            coVerify(exactly = 0) { // 값이 비어있었으면 UseCase는 실행되지 않았어야 한다.
                addUserUseCase(name = any(), age = any())
            }

            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickListButton Event Test`() = runTest {
        val navigator = FakeNavigator(MainScreen)
        MainScreenPresenter(
            navigator = navigator,
            addUserUseCase = addUserUseCase
        ).test {
            val state = awaitItem()
            state.eventSink(MainScreen.State.MainScreenEvent.OnClickListButton)
            navigator.awaitNextScreen() shouldBe ListScreen

            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }
}