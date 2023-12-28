package com.example.mvisampleapp.domain.repository

import com.example.mvisampleapp.data.model.dao.UserDao
import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    override fun getAllUser(): Flow<List<User>> {
        return userDao.getUserList()
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user = user)
    }

    override suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}
