package com.example.data_impl.repository

import com.example.data.db.dao.UserDao
import com.example.data.db.entity.UserEntity
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    override fun getAllUser(): Flow<List<UserEntity>> {
        return userDao.getUserList()
    }

    override suspend fun deleteUser(name: String, age: Int) {
        userDao.deleteUser(
            name = name,
            age = age,
        )
    }

    override suspend fun addUser(userEntity: UserEntity) {
        userDao.addUser(userEntity)
    }

    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
