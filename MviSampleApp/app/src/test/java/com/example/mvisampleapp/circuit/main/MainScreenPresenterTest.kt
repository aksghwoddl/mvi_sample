package com.example.mvisampleapp.circuit.main

import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.presenter.MainScreenPresenter
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.utils.shouldBe
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainScreenPresenterTest : BaseTest() {
    @MockK
    lateinit var addUserUseCase: AddUserUseCase

    @Test
    fun `OnClickAddUserButton Event 성공 Test`() = runTest {
        MainScreenPresenter(
            navigator = FakeNavigator(),
            addUserUseCase = addUserUseCase
        ).test {
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age = "9"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserName(name = "테스트"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)
            skipItems(2)
            with(awaitItem()) {
                mainModel.name shouldBe ""
                mainModel.age shouldBe ""
                mainModel.alertMessage shouldBe "정상적으로 값이 저장 되었습니다!"
            }
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickAddUserButton Event 실패 Test`() = runTest {
        MainScreenPresenter(
            navigator = FakeNavigator(),
            addUserUseCase = addUserUseCase
        ).test {
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age = "9"))
            awaitItem().eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)
            awaitItem().mainModel.alertMessage shouldBe "값을 확인 해주세요!"
            cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        }
    }

    @Test
    fun `OnClickListButton Event Test`() = runTest {
        val navigator = FakeNavigator()
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