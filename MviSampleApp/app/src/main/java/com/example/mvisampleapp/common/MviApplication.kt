package com.example.mvisampleapp.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MviApplication : Application() {
    companion object {
        private lateinit var instance: MviApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = MviApplication()
    }
}
