package com.example.mvisampleapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvisampleapp.data.db.dao.UserDao
import com.example.mvisampleapp.data.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
