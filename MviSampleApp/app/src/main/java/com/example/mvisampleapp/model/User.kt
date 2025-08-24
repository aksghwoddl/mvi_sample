package com.example.mvisampleapp.model

import androidx.compose.runtime.Stable

@Stable
data class User(
    val name: String,
    val age: Int,
)