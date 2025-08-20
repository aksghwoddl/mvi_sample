package com.example.mvisampleapp.data.repository

import com.example.mvisampleapp.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUser(): Flow<List<UserEntity>>

    suspend fun deleteUser(name: String, age: Int)

    suspend fun addUser(userEntity: UserEntity)

    suspend fun deleteAll()
}
