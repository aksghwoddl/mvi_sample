package com.example.mvisampleapp.ui.circuit.main.screen

import com.example.mvisampleapp.ui.circuit.main.model.MainModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
object MainScreen : Screen {
    data class State(
        val mainModel: MainModel,
        val eventSink: (MainScreenEvent) -> Unit = {},
    ) : CircuitUiState {
        sealed interface MainScreenEvent : CircuitUiEvent {
            data class OnSetUserName(val name: String) : MainScreenEvent
            data class OnSetUserAge(val age: String) : MainScreenEvent
            object OnClickAddUserButton : MainScreenEvent
            object OnClickListButton : MainScreenEvent
            object OnShowSnackBar : MainScreenEvent
        }
    }
}
