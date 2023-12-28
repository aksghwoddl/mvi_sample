package com.example.mvisampleapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvisampleapp.data.model.dao.UserDao
import com.example.mvisampleapp.data.model.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
