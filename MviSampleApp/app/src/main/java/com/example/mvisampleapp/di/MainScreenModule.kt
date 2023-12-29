package com.example.mvisampleapp.di

import com.example.mvisampleapp.ui.circuit.createCircuitScreenUiFactory
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
class MainScreenModule {
    @Provides
    @IntoSet
    @CircuitModule.MainCircuit
    fun provideMainScreenUiFactory() : Ui.Factory {
        return createCircuitScreenUiFactory<MainScreen , MainScreen.State> { state, modifier ->

        }
    }

    @Provides
    @IntoSet
    @CircuitModule.MainCircuit
    fun provideMainScreenPresenterFactory() : Presenter.Factory {
    }
}