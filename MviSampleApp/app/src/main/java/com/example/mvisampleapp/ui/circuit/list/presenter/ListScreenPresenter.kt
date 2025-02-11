package com.example.mvisampleapp.ui.circuit.list.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mvisampleapp.common.Async
import com.example.mvisampleapp.common.produceAsync
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.circuit.list.model.ListModel
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

private const val TAG = "ListScreenPresenter"

class ListScreenPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val getUserListUseCase: GetUserListUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : Presenter<ListScreen.State> {
    @Composable
    override fun present(): ListScreen.State {
        var listModel by rememberRetained {
            mutableStateOf(ListModel.placeHolder)
        }

        var userListState: Async<List<User>> by rememberRetained {
            mutableStateOf(Async.Loading)
        }

        var deleteUserState: Async<Unit> by rememberRetained {
            mutableStateOf(Async.None)
        }

        userListState = produceAsync(
            async = userListState,
            producer = {
                getUserListUseCase().first()
            },
            onSuccess = { list ->
                listModel = listModel.copy(
                    userList = list
                )
                Async.Success(data = list)
            },
            onFail = { throwable ->
                listModel = listModel.copy(
                    userList = emptyList(),
                    alertMessage = "문제가 발생 했습니다. 로그를 확인하세요."
                )
                Async.Fail(throwable = throwable)
            }
        )

        deleteUserState = produceAsync(
            async = deleteUserState,
            producer = {
                listModel.selectedUser?.let { user ->
                    deleteUserUseCase(user = user)
                }
            },
            onSuccess = {
                listModel = listModel.copy(
                    selectedUser = null
                )
                userListState = Async.Loading
                Async.Success(Unit)
            },
            onFail = { throwable ->
                listModel = listModel.copy(
                    alertMessage = "문제가 발생 했습니다. 로그를 확인하세요."
                )
                Async.Fail(throwable = throwable)
            }
        )

        return ListScreen.State(
            listModel = listModel,
        ) { event ->
            when (event) {
                is ListScreen.State.ListScreenEvent.OnClickPreviousButton -> {
                    navigator.goTo(MainScreen)
                }

                is ListScreen.State.ListScreenEvent.OnUpdateUserList -> {
                    userListState = Async.Loading
                }

                is ListScreen.State.ListScreenEvent.OnClickUserItem -> {
                    listModel = listModel.copy(
                        selectedUser = event.user,
                    )
                }

                is ListScreen.State.ListScreenEvent.OnClickDeleteButton -> {
                    deleteUserState = Async.Loading
                }

                is ListScreen.State.ListScreenEvent.OnShowSnackBar -> {
                    listModel = listModel.copy(
                        alertMessage = ""
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface ListScreenAssistedFactory {
        fun create(navigator: Navigator): ListScreenPresenter
    }
}
