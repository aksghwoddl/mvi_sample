package com.example.mvisampleapp.ui.feature.main.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.common.runSuspendCatching
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.base.BaseViewModel
import com.example.mvisampleapp.ui.feature.main.model.MainScreenElements
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

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
            is MainScreenElements.MainScreenEvent.OnSetUserName -> {
                updateState {
                    it.copy(
                        name = event.name,
                    )
                }
            }

            is MainScreenElements.MainScreenEvent.OnSetUserAge -> {
                updateState {
                    it.copy(
                        age = event.age,
                    )
                }
            }

            is MainScreenElements.MainScreenEvent.OnClickAddUserButton -> {
                if (state.value.name.isEmpty() || state.value.age.isEmpty()) { // 값이 하나라도 비어 있으면 알림 보내기
                    Log.d("MainViewModel", "MainViewModel: value empty")
                    sendEffect(
                        MainScreenElements.MainScreenEffect.ShowSnackBar(
                            message = "값을 확인 해주세요!"
                        )
                    )
                } else {
                    val user = User(
                        index = null,
                        name = state.value.name,
                        age = state.value.age.toInt(),
                    )
                    handleSideEffect(MainScreenElements.MainScreenSideEffect.AddUser(user))
                }
            }

            is MainScreenElements.MainScreenEvent.OnClickListButton -> {
                sendEffect(MainScreenElements.MainScreenEffect.MoveListScreen)
            }

            is MainScreenElements.MainScreenEvent.OnAddUserSuccess -> {
                updateState {
                    it.copy(
                        name = "",
                        age = "",
                    )
                }
                sendEffect(
                    MainScreenElements.MainScreenEffect.ShowSnackBar(
                        message = "정상적으로 저장되었습니다!"
                    )
                )
            }
        }
    }

    private fun handleSideEffect(effect: MainScreenElements.MainScreenSideEffect) {
        when (effect) {
            is MainScreenElements.MainScreenSideEffect.AddUser -> {
                addUser(user = effect.user)
            }
        }
    }

    private fun sendEffect(effect: MainScreenElements.MainScreenEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            runSuspendCatching {
                addUserUseCase(user)
            }.onSuccess {
                handleEvent(MainScreenElements.MainScreenEvent.OnAddUserSuccess)
            }.onFailure { throwable ->
                Log.d(TAG, "addUser: fail => $throwable")
            }
        }
    }
}
