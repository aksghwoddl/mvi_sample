package com.example.mvisampleapp.ui.feature.list.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.base.BaseViewModel
import com.example.mvisampleapp.ui.feature.list.model.ListScreenElements
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
            is ListScreenElements.ListScreenEvent.OnClickPreviousButton -> {
                sendEffect(ListScreenElements.ListScreenEffect.MoveMainScreen)
            }

            is ListScreenElements.ListScreenEvent.OnUpdateUserList -> {
                updateState {
                    it.copy(
                        userList = event.userList,
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickUserItem -> {
                updateState {
                    it.copy(
                        selectedUser = event.user,
                    )
                }
                sendEffect(ListScreenElements.ListScreenEffect.ShowDeleteDialog)
            }

            is ListScreenElements.ListScreenEvent.OnClickDeleteButton -> {
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
                getUserList()
            }

            is ListScreenElements.ListScreenSideEffect.DeleteUser -> {
                deleteUser(user = sideEffect.user)
            }
        }
    }

    private fun sendEffect(effect: ListScreenElements.ListScreenEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun getUserList() {
        viewModelScope.launch {
            getUserListUseCase().collect { result ->
                handleEvent(ListScreenElements.ListScreenEvent.OnUpdateUserList(result))
            }
        }
    }

    private fun deleteUser(user: User) {
        viewModelScope.launch {
            deleteUserUseCase(user = user)
            handleSideEffect(ListScreenElements.ListScreenSideEffect.GetUserList)
        }
    }
}
