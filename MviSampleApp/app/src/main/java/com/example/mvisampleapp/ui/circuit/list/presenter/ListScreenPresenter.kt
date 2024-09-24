package com.example.mvisampleapp.ui.circuit.list.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.mvisampleapp.common.runSuspendCatching
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.DeleteUserUseCase
import com.example.mvisampleapp.domain.usecase.GetUserListUseCase
import com.example.mvisampleapp.ui.circuit.list.model.ListModel
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.rememberImpressionNavigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val rememberImpressionNavigator = rememberImpressionNavigator(navigator = navigator) {
            Log.d(TAG, "present: re-enter ListScreen")
        }

        val scope = rememberCoroutineScope()

        return ListScreen.State(
            listModel = listModel,
        ) { event ->
            when (event) {
                is ListScreen.State.ListScreenEvent.OnClickPreviousButton -> {
                    rememberImpressionNavigator.goTo(MainScreen)
                }

                is ListScreen.State.ListScreenEvent.OnUpdateUserList -> {
                    scope.launch {
                        listModel = listModel.copy(
                            userList = getUserList(),
                        )
                    }
                }

                is ListScreen.State.ListScreenEvent.OnClickUserItem -> {
                    listModel = listModel.copy(
                        selectedUser = event.user,
                    )
                }

                is ListScreen.State.ListScreenEvent.OnClickDeleteButton -> {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            deleteUserUseCase(event.user)
                        }
                        listModel = listModel.copy(
                            userList = getUserList(),
                            selectedUser = null,
                        )
                    }
                }
            }
        }
    }

    private suspend fun getUserList(): List<User> {
        runSuspendCatching {
            withContext(Dispatchers.IO) {
                getUserListUseCase()
            }
        }.onSuccess { flow ->
            return flow.first()
        }.onFailure { throwable ->
            Log.d(TAG, "getUserList: fail  => ${throwable.message}")
            return emptyList()
        }
        return emptyList()
    }

    @AssistedFactory
    interface ListScreenAssistedFactory {
        fun create(navigator: Navigator): ListScreenPresenter
    }
}
