package com.example.mvisampleapp.ui.circuit.main.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.domain.usecase.AddUserUseCase
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.circuit.main.model.MainModel
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
                is MainScreen.State.MainScreenEvent.SetUserName -> {
                    mainModel = mainModel.copy(name = event.name)
                }

                is MainScreen.State.MainScreenEvent.SetUserAge -> {
                    mainModel = mainModel.copy(age = event.age)
                }

                is MainScreen.State.MainScreenEvent.ClickAddUserButton -> {
                    val user = User(
                        index = null,
                        name = mainModel.name,
                        age = mainModel.age.toInt(),
                    )
                    scope.launch(Dispatchers.IO) {
                        addUserUseCase(user = user)
                    }
                    mainModel = mainModel.copy(
                        name = "",
                        age = "",
                    )
                }

                is MainScreen.State.MainScreenEvent.ClickListButton -> {
                    rememberImpressionNavigator.goTo(ListScreen)
                }
            }
        }
    }

    @AssistedFactory
    interface MainScreenPresenterAssistedFactory {
        fun create(navigator: Navigator): MainScreenPresenter
    }
}
