package com.example.mvisampleapp.ui.circuit.list.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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

private const val TAG = "ListScreenPresenter"

class ListScreenPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val getUserListUseCase: GetUserListUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : Presenter<ListScreen.State> {
    @Composable
    override fun present(): ListScreen.State {
        var deleteUserState: Async<Unit> by rememberRetained {
            mutableStateOf(Async.None)
        }

        var selectedUser by rememberRetained { mutableStateOf<User?>(null) }

        val userList by produceState(initialValue = emptyList()) {
            getUserListUseCase().collect {
                value = it
            }
        }

        deleteUserState = produceAsync(
            async = deleteUserState,
            producer = {
                selectedUser?.let { user ->
                    deleteUserUseCase(user = user)
                }
            },
            onSuccess = {
                selectedUser = null
                Async.Success(Unit)
            },
            onFail = { throwable ->
                Async.Fail(throwable = throwable)
            }
        )

        return ListScreen.State(
            listModel = ListModel(
                userList = userList,
                selectedUser = selectedUser,
            ),
        ) { event ->
            when (event) {
                is ListScreen.State.ListScreenEvent.OnClickPreviousButton -> {
                    navigator.goTo(MainScreen)
                }

                is ListScreen.State.ListScreenEvent.OnClickUserItem -> {
                    selectedUser = event.user
                }

                is ListScreen.State.ListScreenEvent.OnClickDeleteButton -> {
                    deleteUserState = Async.Loading
                }
            }
        }
    }

    @AssistedFactory
    interface ListScreenAssistedFactory {
        fun create(navigator: Navigator): ListScreenPresenter
    }
}
