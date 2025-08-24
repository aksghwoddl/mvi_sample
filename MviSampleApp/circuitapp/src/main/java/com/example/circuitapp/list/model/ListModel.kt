package com.example.circuitapp.list.model

import androidx.compose.runtime.Stable
import com.example.mvisampleapp.ui.model.User

@Stable
data class ListModel(
    val userList: List<User>,
    val selectedUser: User?,
) {
    companion object {
        val placeHolder = ListModel(
            userList = emptyList(),
            selectedUser = null,
        )
    }
}
