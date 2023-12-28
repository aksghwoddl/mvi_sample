package com.example.mvisampleapp.ui.main.model

import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.ui.base.BaseEvent
import com.example.mvisampleapp.ui.base.BaseState

class MainScreenElements {
    data class MainScreenUiState(
        val name: String = "",
        val age: String = "",
    ) : BaseState

    sealed interface MainScreenEvent : BaseEvent {
        data class SetUserName(val name: String) : MainScreenEvent
        data class SetUserAge(val age: String) : MainScreenEvent
        object ClickAddUserButton : MainScreenEvent
        object ClickListButton : MainScreenEvent
    }

    sealed interface MainScreenEffect {
        data class ShowSnackBar(val message: String) : MainScreenEffect
        object MoveListScreen : MainScreenEffect
    }

    sealed interface MainScreenSideEffect {
        data class AddUser(val user: User) : MainScreenSideEffect
    }
}
