package com.example.circuitapp.list.model

import androidx.compose.runtime.Stable
import com.example.presenter.feature.list.model.User
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ListModel(
    val userList: PersistentList<User>,
    val selectedUser: User?,
) {
    companion object {
        val placeHolder = ListModel(
            userList = persistentListOf(),
            selectedUser = null,
        )
    }
}
