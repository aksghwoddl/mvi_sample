package com.example.mvisampleapp.feature.main

import app.cash.turbine.testIn
import com.example.mvisampleapp.base.BaseTest
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.feature.main.model.MainScreenElements
import com.example.mvisampleapp.feature.main.viewmodel.MainViewModel
import com.example.mvisampleapp.utils.shouldBe
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainViewModelTest : BaseTest() {
    @MockK
    lateinit var addUserUseCase: AddUserUseCase

    private lateinit var viewModel: MainViewModel

    override fun setup() {
        super.setup()
        viewModel = MainViewModel(addUserUseCase)
    }

    @Test
    fun `OnClickAddUserButton Event 성공 Test`() = runTest {
        val effect = viewModel.effect.testIn(this)
        val state = viewModel.state.testIn(this)

        state.skipItems(1) // 기본값은 생략

        viewModel.run {
            handleEvent(MainScreenElements.MainScreenEvent.OnSetUserAge(age = "9"))
            state.awaitItem().age shouldBe "9"
            handleEvent(MainScreenElements.MainScreenEvent.OnSetUserName(name = "테스트"))
            state.awaitItem().name shouldBe "테스트"
            handleEvent(MainScreenElements.MainScreenEvent.OnClickAddUserButton)
            with(state.awaitItem()) {
                age shouldBe ""
                name shouldBe ""
            }
        }

        with(effect.awaitItem()) {
            if (this is MainScreenElements.MainScreenEffect.ShowSnackBar) {
                this.message shouldBe "정상적으로 저장되었습니다!"
            }
        }

        effect.cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
        state.cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
    }

    @Test
    fun `OnClickAddUserButton Event 실패 Test`() = runTest {
        val effect = viewModel.effect.testIn(this)
        viewModel.run {
            handleEvent(MainScreenElements.MainScreenEvent.OnSetUserAge(age = ""))
            handleEvent(MainScreenElements.MainScreenEvent.OnSetUserName(name = ""))
            handleEvent(MainScreenElements.MainScreenEvent.OnClickAddUserButton)
        }

        with(effect.awaitItem()) {
            if (this is MainScreenElements.MainScreenEffect.ShowSnackBar) {
                this.message shouldBe "값을 확인 해주세요!"
            }
        }

        effect.cancelAndConsumeRemainingEvents().isEmpty() shouldBe true
    }
}