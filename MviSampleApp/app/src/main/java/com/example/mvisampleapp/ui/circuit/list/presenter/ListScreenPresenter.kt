package com.example.mvisampleapp.ui.circuit.list.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
                is ListScreen.State.ListScreenEvent.ClickPreviousButton -> {
                    rememberImpressionNavigator.goTo(MainScreen)
                }

                is ListScreen.State.ListScreenEvent.UpdateUserList -> {
                    scope.launch {
                        getUserListUseCase().collect { userList ->
                            listModel = listModel.copy(
                                userList = userList,
                            )
                        }
                    }
                }

                is ListScreen.State.ListScreenEvent.ClickUserItem -> {
                    listModel = listModel.copy(
                        selectedUser = event.user,
                    )
                }

                is ListScreen.State.ListScreenEvent.ClickDeleteButton -> {
                    scope.launch {
                        deleteUserUseCase(event.user)
                        getUserListUseCase().collect { userList ->
                            listModel = listModel.copy(
                                userList = userList,
                                selectedUser = null,
                            )
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface ListScreenAssistedFactory {
        fun create(navigator: Navigator): ListScreenPresenter
    }
}
