package com.example.mvisampleapp.ui.feature.list.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.common.runSuspendCatching
import com.example.mvisampleapp.domain.model.UserModel
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.base.BaseViewModel
import com.example.mvisampleapp.ui.feature.list.model.ListScreenElements
import com.example.mvisampleapp.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ListViewModel"

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
            runSuspendCatching {
                getUserListUseCase()
            }.onSuccess { flow ->
                flow.collect { result ->
                    val userList = withContext(Dispatchers.Default) {
                        result.map {
                            it.toUser()
                        }
                    }
                    handleEvent(ListScreenElements.ListScreenEvent.OnUpdateUserList(userList))
                }
            }.onFailure { throwable ->
                Log.d(TAG, "getUserList : fail => ${throwable.message}")
            }
        }
    }

    private fun deleteUser(user: User) {
        viewModelScope.launch {
            runSuspendCatching {
                deleteUserUseCase(
                    name = user.name,
                    age = user.age,
                )
            }.onSuccess {
                handleSideEffect(ListScreenElements.ListScreenSideEffect.GetUserList)
            }.onFailure { throwable ->
                Log.d(TAG, "deleteUser: fail => ${throwable.message}")
            }
        }
    }

    private fun UserModel.toUser() = User(
        name = name,
        age = age,
    )
}
