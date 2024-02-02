package com.example.mvisampleapp.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mvisampleapp.data.database.UserDatabase
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FakeUserRepository : UserRepository {
    private val database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().context,
        UserDatabase::class.java,
    ).build()

    override fun getAllUser(): Flow<List<User>> {
        return database.userDao().getUserList()
    }

    override suspend fun deleteUser(user: User) {
        database.userDao().deleteUser(user = user)
    }

    override suspend fun addUser(user: User) {
        database.userDao().addUser(user = user)
    }

    fun closeDatabase() {
        database.close()
    }
}
