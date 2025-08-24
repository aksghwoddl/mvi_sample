package com.example.circuitapp.di

import com.example.mvisampleapp.ui.circuit.createCircuitScreenPresenterFactory
import com.example.mvisampleapp.ui.circuit.createCircuitScreenUiFactory
import com.example.mvisampleapp.ui.circuit.list.List
import com.example.mvisampleapp.ui.circuit.list.presenter.ListScreenPresenter
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
class ListScreenModule {
    @Provides
    @IntoSet
    @CircuitModule.MainCircuit
    fun provideListScreenUiFactory(): Ui.Factory {
        return createCircuitScreenUiFactory<ListScreen, ListScreen.State> { state, modifier ->
            List(state = state, modifier = modifier)
        }
    }

    @Provides
    @IntoSet
    @CircuitModule.MainCircuit
    fun provideListScreenPresenterFactory(
        listScreenPresenterAssistedFactory: ListScreenPresenter.ListScreenAssistedFactory,
    ): Presenter.Factory {
        return createCircuitScreenPresenterFactory<ListScreen, ListScreen.State> { _, navigator, _ ->
            listScreenPresenterAssistedFactory.create(navigator = navigator)
        }
    }
}
