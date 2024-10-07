package com.example.mvisampleapp.ui.circuit.list.model

import androidx.compose.runtime.Stable
import com.example.mvisampleapp.data.model.entity.User

@Stable
data class ListModel(
    val userList: List<User>,
    val selectedUser: User?,
    val alertMessage: String
) {
    companion object {
        val placeHolder = ListModel(
            userList = emptyList(),
            selectedUser = null,
            alertMessage = ""
        )
    }
}
