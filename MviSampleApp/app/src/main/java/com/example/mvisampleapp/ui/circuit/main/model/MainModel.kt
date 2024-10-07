package com.example.mvisampleapp.ui.circuit.main.model

import androidx.compose.runtime.Stable

@Stable
data class MainModel(
    val name: String,
    val age: String,
    val alertMessage: String,
) {
    companion object {
        val placeHolder = MainModel(
            name = "",
            age = "",
            alertMessage = ""
        )
    }
}
