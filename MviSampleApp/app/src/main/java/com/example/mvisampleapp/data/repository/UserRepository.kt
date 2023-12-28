package com.example.mvisampleapp.data.repository

import com.example.mvisampleapp.data.model.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUser(): Flow<List<User>>

    suspend fun deleteUser(user: User)

    suspend fun addUser(user: User)
}
