package com.example.mvisampleapp.ui.circuit.main.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.mvisampleapp.common.runSuspendCatching
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.model.MainModel
import com.example.mvisampleapp.ui.circuit.main.model.SnackBarState
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.rememberImpressionNavigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainScreenPresenter"

class MainScreenPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val addUserUseCase: AddUserUseCase,
) : Presenter<MainScreen.State> {
    @Composable
    override fun present(): MainScreen.State {
        var mainModel by rememberRetained {
            mutableStateOf(MainModel.placeHolder)
        }

        val rememberImpressionNavigator = rememberImpressionNavigator(navigator = navigator) {
            Log.d(TAG, "present: re-enter MainScreen")
        }

        val scope = rememberCoroutineScope()

        return MainScreen.State(
            mainModel = mainModel,
        ) { event ->
            when (event) {
                is MainScreen.State.MainScreenEvent.OnSetUserName -> {
                    mainModel = mainModel.copy(name = event.name)
                }

                is MainScreen.State.MainScreenEvent.OnSetUserAge -> {
                    mainModel = mainModel.copy(age = event.age)
                }

                is MainScreen.State.MainScreenEvent.OnClickAddUserButton -> {
                    if (mainModel.name.isNotEmpty() && mainModel.age.isNotEmpty()) { // 값이 비어 있지 않으면
                        scope.launch {
                            val isSuccess = withContext(Dispatchers.IO) {
                                addUser(
                                    name = mainModel.name,
                                    age = mainModel.age.toIntOrNull() ?: 0
                                )
                            }
                            mainModel = if (isSuccess) {
                                MainModel(
                                    name = "",
                                    age = "",
                                    snackBarState = SnackBarState(message = "정상적으로 값이 저장 되었습니다!")
                                )
                            } else {
                                MainModel(
                                    snackBarState = SnackBarState(message = "문제가 발생 했습니다!")
                                )
                            }
                        }
                    } else { // 값이 비어 있을때
                        mainModel = mainModel.copy(
                            snackBarState = SnackBarState(message = "값을 확인 해주세요!")
                        )
                    }
                }

                is MainScreen.State.MainScreenEvent.OnClickListButton -> {
                    rememberImpressionNavigator.goTo(ListScreen)
                }

                is MainScreen.State.MainScreenEvent.OnShowSnackBar -> {
                    mainModel = mainModel.copy(
                        snackBarState = SnackBarState(
                            message = ""
                        )
                    )
                }
            }
        }
    }

    private suspend fun addUser(
        name: String,
        age: Int
    ): Boolean {
        runSuspendCatching {
            withContext(Dispatchers.IO) {
                addUserUseCase(
                    user = User(
                        index = null,
                        name = name,
                        age = age,
                    )
                )
            }
        }.onSuccess { // 성공적으로 저장 했을때
            return true
        }.onFailure { throwable -> // 예외 발생
            Log.d(TAG, "addUser: fail => ${throwable.message}")
            return false
        }
        return false
    }

    @AssistedFactory
    interface MainScreenPresenterAssistedFactory {
        fun create(navigator: Navigator): MainScreenPresenter
    }
}
