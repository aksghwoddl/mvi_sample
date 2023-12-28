package com.example.mvisampleapp.ui.list.model

import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.ui.base.BaseEvent
import com.example.mvisampleapp.ui.base.BaseState

class ListScreenElements {
    data class ListScreenState(
        val userList: List<User> = emptyList(),
        val selectedUser: User? = null,
    ) : BaseState

    sealed interface ListScreenEvent : BaseEvent {
        object ClickPreviousButton : ListScreenEvent
        data class UpdateUserList(val userList: List<User>) : ListScreenEvent
        data class ClickUserItem(val user: User) : ListScreenEvent
        data class ClickDeleteButton(val user: User) : ListScreenEvent
    }

    sealed interface ListScreenEffect {
        data class ShowSnackBar(val message: String) : ListScreenEffect
        object MoveMainScreen : ListScreenEffect
        object ShowDeleteDialog : ListScreenEffect
    }

    sealed interface ListScreenSideEffect {
        object GetUserList : ListScreenSideEffect
        data class DeleteUser(val user: User) : ListScreenSideEffect
    }
}
