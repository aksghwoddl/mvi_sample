package com.example.mvisampleapp.feature.list.model

import androidx.compose.runtime.Immutable
import com.example.mvisampleapp.base.BaseEvent
import com.example.mvisampleapp.base.BaseState
import com.example.presenter.feature.list.model.User
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

class ListScreenElements {
    data class ListScreenState(
        val userList: PersistentList<User> = persistentListOf(),
        val selectedUser: User? = null,
    ) : BaseState

    sealed interface ListScreenEvent : BaseEvent {
        object OnClickPreviousButton : ListScreenEvent
        data class OnUpdateUserList(val userList: List<User>) : ListScreenEvent
        data class OnClickUserItem(val user: User) : ListScreenEvent
        data class OnClickDeleteButton(val user: User) : ListScreenEvent
    }

    @Immutable
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
