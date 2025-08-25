package com.example.circuitapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

inline fun <reified T : Screen, S : CircuitUiState> createCircuitScreenUiFactory(
    crossinline body: @Composable (state: S, modifier: Modifier) -> Unit,
): Ui.Factory {
    return Ui.Factory { screen, _ ->
        when (screen) {
            is T -> ui<S> { state, modifier -> body(state, modifier) }
            else -> null
        }
    }
}

inline fun <reified T : Screen, S : CircuitUiState> createCircuitScreenPresenterFactory(
    crossinline presenter: (T, Navigator, CircuitContext) -> Presenter<S>,
): Presenter.Factory {
    return Presenter.Factory { screen, navigator, context ->
        when (screen) {
            is T -> presenter(screen, navigator, context)
            else -> null
        }
    }
}
