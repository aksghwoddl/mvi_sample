package com.example.mvisampleapp.feature.list.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserModel
import com.example.domain.usecase.DeleteAllUserUseCase
import com.example.domain.usecase.DeleteUserUseCase
import com.example.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.base.BaseViewModel
import com.example.mvisampleapp.common.runSuspendCatching
import com.example.mvisampleapp.feature.list.model.ListScreenElements
import com.example.presenter.feature.list.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
    private val deleteAllUserUseCase: DeleteAllUserUseCase
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
                        userList = event.userList.toPersistentList(),
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickUserItem -> {
                updateState {
                    it.copy(
                        selectedUser = event.user,
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickDeleteButton -> {
                handleSideEffect(ListScreenElements.ListScreenSideEffect.DeleteUser(event.user))
                updateState {
                    it.copy(
                        selectedUser = null,
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.ShowUserDeleteDialog -> {
                updateState {
                    it.copy(
                        isShowUserDeleteDialog = true
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.DismissUserDeleteDialog -> {
                updateState {
                    it.copy(
                        selectedUser = null,
                        isShowUserDeleteDialog = false
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickDeleteAllButton -> {
                updateState {
                    it.copy(
                        isShowAllUserDeleteDialog = true
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickDeleteAllConfirmButton -> {
                handleSideEffect(ListScreenElements.ListScreenSideEffect.DeleteAll)
                updateState {
                    it.copy(
                        isShowAllUserDeleteDialog = false
                    )
                }
            }

            is ListScreenElements.ListScreenEvent.OnClickDeleteAllCancelButton -> {
                updateState {
                    it.copy(
                        isShowAllUserDeleteDialog = false
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

            is ListScreenElements.ListScreenSideEffect.DeleteAll -> {
                deleteAllUser()
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
            }.onSuccess {
                handleEvent(
                    ListScreenElements.ListScreenEvent.OnUpdateUserList(
                        userList = withContext(
                            Dispatchers.Default
                        ) {
                            it.map { userModel ->
                                userModel.toUser()
                            }
                        })
                )
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

    private fun deleteAllUser() {
        viewModelScope.launch {
            runSuspendCatching {
                deleteAllUserUseCase()
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
