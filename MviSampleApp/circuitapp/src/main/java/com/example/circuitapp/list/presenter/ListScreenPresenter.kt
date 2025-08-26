package com.example.circuitapp.list.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import com.example.circuitapp.Async
import com.example.circuitapp.list.model.ListModel
import com.example.circuitapp.list.screen.ListScreen
import com.example.circuitapp.main.screen.MainScreen
import com.example.circuitapp.produceAsync
import com.example.domain.model.UserModel
import com.example.domain.usecase.DeleteUserUseCase
import com.example.domain.usecase.GetUserListFlowUseCase
import com.example.presenter.feature.list.model.User
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ListScreenPresenter"

class ListScreenPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val getUserListFlowUseCase: GetUserListFlowUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : Presenter<ListScreen.State> {
    @Composable
    override fun present(): ListScreen.State {
        var deleteUserState: Async<Unit> by rememberRetained {
            mutableStateOf(Async.None)
        }

        var selectedUser by rememberRetained { mutableStateOf<User?>(null) }
        var isShowUserDeleteDialog by rememberRetained { mutableStateOf(false) }

        val userList by produceState(initialValue = emptyList()) {
            getUserListFlowUseCase().collect {
                value = withContext(Dispatchers.Default) {
                    it.map { userModel ->
                        userModel.toUser()
                    }
                }
            }
        }

        deleteUserState = produceAsync(
            async = deleteUserState,
            producer = {
                selectedUser?.let { user ->
                    deleteUserUseCase(
                        name = user.name,
                        age = user.age,
                    )
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
                userList = userList.toPersistentList(),
                selectedUser = selectedUser,
                isShowUserDeleteDialog = isShowUserDeleteDialog
            ),
        ) { event ->
            when (event) {
                is ListScreen.State.ListScreenEvent.OnClickPreviousButton -> {
                    navigator.goTo(MainScreen)
                }

                is ListScreen.State.ListScreenEvent.OnClickUserItem -> {
                    isShowUserDeleteDialog = true
                    selectedUser = event.user
                }

                is ListScreen.State.ListScreenEvent.OnClickDeleteButton -> {
                    deleteUserState = Async.Loading
                    isShowUserDeleteDialog = false
                }

                is ListScreen.State.ListScreenEvent.ShowUserDeleteDialog -> {
                    isShowUserDeleteDialog = true
                }

                is ListScreen.State.ListScreenEvent.DismissUserDeleteDialog -> {
                    selectedUser = null
                    isShowUserDeleteDialog = false
                }
            }
        }
    }

    private fun UserModel.toUser() = User(
        name = name,
        age = age,
    )

    @AssistedFactory
    interface ListScreenAssistedFactory {
        fun create(navigator: Navigator): ListScreenPresenter
    }
}
