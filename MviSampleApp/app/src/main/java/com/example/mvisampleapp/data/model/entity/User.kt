package com.example.mvisampleapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val index: Int? = null,
    val name: String,
    val age: Int,
)
