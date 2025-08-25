package com.example.circuitapp.di

import com.example.circuitapp.createCircuitScreenPresenterFactory
import com.example.circuitapp.createCircuitScreenUiFactory
import com.example.circuitapp.main.Main
import com.example.circuitapp.main.presenter.MainScreenPresenter
import com.example.circuitapp.main.screen.MainScreen
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
    fun provideMainScreenUiFactory(): Ui.Factory {
        return createCircuitScreenUiFactory<MainScreen, MainScreen.State> { state, modifier ->
            Main(
                state = state,
                modifier = modifier,
            )
        }
    }

    @Provides
    @IntoSet
    @CircuitModule.MainCircuit
    fun provideMainScreenPresenterFactory(
        mainScreenPresenterAssistedFactory: MainScreenPresenter.MainScreenPresenterAssistedFactory,
    ): Presenter.Factory {
        return createCircuitScreenPresenterFactory<MainScreen, MainScreen.State> { _, navigator, _ ->
            mainScreenPresenterAssistedFactory.create(navigator = navigator)
        }
    }
}
