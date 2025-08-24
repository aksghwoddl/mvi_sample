package com.example.circuitapp.main.model

import androidx.compose.runtime.Stable

@Stable
data class MainModel(
    val name: String,
    val age: String,
) {
    companion object {
        val placeHolder = MainModel(
            name = "",
            age = "",
        )
    }
}
