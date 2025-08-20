package com.example.mvisampleapp.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mvisampleapp.data.db.UserDatabase
import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FakeUserRepository : UserRepository {
    private val database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().context,
        UserDatabase::class.java,
    ).build()

    override fun getAllUser(): Flow<List<UserEntity>> {
        return database.userDao().getUserList()
    }

    override suspend fun deleteUser(name: String, age: Int) {
        database.userDao().deleteUser(
            name = name,
            age = age,
        )
    }

    override suspend fun addUser(userEntity: UserEntity) {
        database.userDao().addUser(userEntity = userEntity)
    }

    override suspend fun deleteAll() {
        database.userDao().deleteAll()
    }

    fun closeDatabase() {
        database.close()
    }
}
