package com.example.mvisampleapp.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.base.BaseViewModel
import com.example.mvisampleapp.ui.main.model.MainScreenElements
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
) : BaseViewModel<MainScreenElements.MainScreenUiState, MainScreenElements.MainScreenEvent>(
    MainScreenElements.MainScreenUiState(),
) {
    private val _effect = MutableSharedFlow<MainScreenElements.MainScreenEffect>()
    val effect: SharedFlow<MainScreenElements.MainScreenEffect> = _effect.asSharedFlow()

    override fun handleEvent(event: MainScreenElements.MainScreenEvent) {
        when (event) {
            is MainScreenElements.MainScreenEvent.SetUserName -> {
                updateState {
                    it.copy(
                        name = event.name,
                    )
                }
            }

            is MainScreenElements.MainScreenEvent.SetUserAge -> {
                updateState {
                    it.copy(
                        age = event.age,
                    )
                }
            }

            is MainScreenElements.MainScreenEvent.ClickAddUserButton -> {
                val user = User(
                    index = null,
                    name = state.value.name,
                    age = state.value.age.toInt(),
                )
                handleSideEffect(MainScreenElements.MainScreenSideEffect.AddUser(user))
                updateState {
                    it.copy(
                        name = "",
                        age = "",
                    )
                }
            }

            is MainScreenElements.MainScreenEvent.ClickListButton -> {
                sendEffect(MainScreenElements.MainScreenEffect.MoveListScreen)
            }
        }
    }

    private fun handleSideEffect(effect: MainScreenElements.MainScreenSideEffect) {
        when (effect) {
            is MainScreenElements.MainScreenSideEffect.AddUser -> {
                viewModelScope.launch {
                    addUserUseCase(effect.user)
                }
            }
        }
    }

    private fun sendEffect(effect: MainScreenElements.MainScreenEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
