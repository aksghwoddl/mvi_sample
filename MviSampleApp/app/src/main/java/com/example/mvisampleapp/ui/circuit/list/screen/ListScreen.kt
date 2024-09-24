package com.example.mvisampleapp.ui.circuit.list.screen

import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.ui.circuit.list.model.ListModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
object ListScreen : Screen {
    data class State(
        val listModel: ListModel,
        val eventSink: (ListScreenEvent) -> Unit = {},
    ) : CircuitUiState {
        sealed interface ListScreenEvent : CircuitUiEvent {
            object OnClickPreviousButton : ListScreenEvent
            object OnUpdateUserList : ListScreenEvent
            data class OnClickUserItem(val user: User) : ListScreenEvent
            data class OnClickDeleteButton(val user: User) : ListScreenEvent
        }
    }
}
