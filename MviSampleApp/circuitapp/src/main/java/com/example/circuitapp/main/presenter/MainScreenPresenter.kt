package com.example.circuitapp.main.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.circuitapp.Async
import com.example.circuitapp.list.screen.ListScreen
import com.example.circuitapp.main.model.MainModel
import com.example.circuitapp.main.screen.MainScreen
import com.example.circuitapp.produceAsync
import com.example.domain.usecase.AddUserUseCase
import com.example.design_system.snackbar.LocalSnackBarEventController
import com.example.design_system.snackbar.SnackBarEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val TAG = "MainScreenPresenter"

class MainScreenPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val addUserUseCase: AddUserUseCase,
) : Presenter<MainScreen.State> {
    @Composable
    override fun present(): MainScreen.State {
        val snackBarEventController = LocalSnackBarEventController.current

        var name by rememberRetained { mutableStateOf("") }
        var age by rememberRetained { mutableStateOf("") }

        var addUserState: Async<Unit> by rememberRetained {
            mutableStateOf(Async.None)
        }

        addUserState = produceAsync(
            async = addUserState,
            producer = {
                addUserUseCase(
                    name = name,
                    age = age.toIntOrNull() ?: 0
                )
            },
            onSuccess = {
                name = ""
                age = ""
                snackBarEventController.sendEvent(SnackBarEvent(message = "값이 정상적으로 저장 되었습니다!"))
                Async.Success(data = Unit)
            },
            onFail = { throwable ->
                snackBarEventController.sendEvent(SnackBarEvent(message = "문제가 발생 했습니다!"))
                Async.Fail(throwable = throwable)
            }
        )

        return MainScreen.State(
            mainModel = MainModel(
                name = name,
                age = age
            ),
        ) { event ->
            when (event) {
                is MainScreen.State.MainScreenEvent.OnSetUserName -> {
                    name = event.name
                }

                is MainScreen.State.MainScreenEvent.OnSetUserAge -> {
                    age = event.age
                }

                is MainScreen.State.MainScreenEvent.OnClickAddUserButton -> {
                    if (name.isNotEmpty() && age.isNotEmpty()) { // 값이 비어 있지 않으면
                        addUserState = Async.Loading
                    } else { // 값이 비어 있을때
                        snackBarEventController.sendEvent(SnackBarEvent(message = "값을 확인해주세요!"))
                    }
                }

                is MainScreen.State.MainScreenEvent.OnClickListButton -> {
                    navigator.goTo(ListScreen)
                }
            }
        }
    }

    @AssistedFactory
    interface MainScreenPresenterAssistedFactory {
        fun create(navigator: Navigator): MainScreenPresenter
    }
}
