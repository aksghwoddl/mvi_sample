package com.example.mvisampleapp.ui.feature.main.model

import androidx.compose.runtime.Immutable
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.ui.base.BaseEvent
import com.example.mvisampleapp.ui.base.BaseState

class MainScreenElements {
    data class MainScreenUiState(
        val name: String = "",
        val age: String = "",
    ) : BaseState

    sealed interface MainScreenEvent : BaseEvent {
        data class OnSetUserName(val name: String) : MainScreenEvent
        data class OnSetUserAge(val age: String) : MainScreenEvent
        object OnClickAddUserButton : MainScreenEvent
        object OnClickListButton : MainScreenEvent
        object OnAddUserSuccess : MainScreenEvent
    }

    @Immutable
    sealed interface MainScreenEffect {
        data class ShowSnackBar(val message: String) : MainScreenEffect
        object MoveListScreen : MainScreenEffect
    }

    sealed interface MainScreenSideEffect {
        data class AddUser(
            val name: String,
            val age: Int,
        ) : MainScreenSideEffect
    }
}
