package com.example.mvisampleapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int? = null,
    val name: String,
    val age: Int,
)
