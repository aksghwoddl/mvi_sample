package com.example.mvisampleapp.ui.list.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.base.BaseViewModel
import com.example.mvisampleapp.ui.list.model.ListScreenElements
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) :
    BaseViewModel<ListScreenElements.ListScreenState, ListScreenElements.ListScreenEvent>(
        ListScreenElements.ListScreenState(),
    ) {

    private val _effect = MutableSharedFlow<ListScreenElements.ListScreenEffect>()
    val effect: SharedFlow<ListScreenElements.ListScreenEffect> = _effect.asSharedFlow()

    init {
        handleSideEffect(ListScreenElements.ListScreenSideEffect.GetUserList)
    }

    override fun handleEvent(event: ListScreenElements.ListScreenEvent) {
        when (event) {
            is ListScreenElements.ListScreenEvent.ClickPreviousButton -> {
                sendEffect(ListScreenElements.ListScreenEffect.MoveMainScreen)
            }

            is ListScreenElements.ListScreenEvent.UpdateUserList -> {
                updateState {
                    it.copy(
                        userList = event.userList,
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.ClickUserItem -> {
                updateState {
                    it.copy(
                        selectedUser = event.user,
                    )
                }
                sendEffect(ListScreenElements.ListScreenEffect.ShowDeleteDialog)
            }

            is ListScreenElements.ListScreenEvent.ClickDeleteButton -> {
                handleSideEffect(ListScreenElements.ListScreenSideEffect.DeleteUser(event.user))
                updateState {
                    it.copy(
                        selectedUser = null,
                    )
                }
            }
        }
    }

    private fun handleSideEffect(sideEffect: ListScreenElements.ListScreenSideEffect) {
        when (sideEffect) {
            is ListScreenElements.ListScreenSideEffect.GetUserList -> {
                viewModelScope.launch {
                    getUserListUseCase().collect { result ->
                        handleEvent(ListScreenElements.ListScreenEvent.UpdateUserList(result))
                    }
                }
            }

            is ListScreenElements.ListScreenSideEffect.DeleteUser -> {
                viewModelScope.launch {
                    deleteUserUseCase(sideEffect.user)
                    handleSideEffect(ListScreenElements.ListScreenSideEffect.GetUserList)
                }
            }
        }
    }

    private fun sendEffect(effect: ListScreenElements.ListScreenEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
