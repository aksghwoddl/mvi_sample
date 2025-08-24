package com.example.circuitapp.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CircuitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainCircuit

    companion object {
        @Provides
        @ActivityRetainedScoped
        @MainCircuit
        fun provideMainCircuit(
            @MainCircuit uiFactories: Set<@JvmSuppressWildcards Ui.Factory>,
            @MainCircuit presenterFactories: Set<@JvmSuppressWildcards Presenter.Factory>,
        ) = Circuit.Builder()
            .addUiFactories(uiFactories)
            .addPresenterFactories(presenterFactories)
            .build()
    }
}
