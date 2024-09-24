package com.example.mvisampleapp.ui.circuit.main.model

import androidx.compose.runtime.Stable

@Stable
data class MainModel(
    val name: String = "",
    val age: String = "",
    val snackBarState: SnackBarState = SnackBarState()
) {
    companion object {
        val placeHolder = MainModel(
            name = "",
            age = "",
        )
    }
}

@Stable
data class SnackBarState(
    val message: String = ""
)
