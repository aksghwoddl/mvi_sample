package com.example.mvisampleapp.di

import android.content.Context
import androidx.room.Room
import com.example.mvisampleapp.common.MviApplication
import com.example.mvisampleapp.data.database.UserDatabase
import com.example.mvisampleapp.data.model.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideApplication(): MviApplication {
        return MviApplication.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = UserDatabase::class.java,
            name = "user.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }
}
