package com.example.mvisampleapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvisampleapp.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserList(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE name = :name AND age = :age")
    suspend fun deleteUser(name: String , age: Int)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
