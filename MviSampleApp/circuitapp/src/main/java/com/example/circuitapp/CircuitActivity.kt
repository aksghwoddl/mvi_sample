package com.example.circuitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.circuitapp.di.CircuitModule
import com.example.circuitapp.main.screen.MainScreen
import com.example.design_system.snackbar.LocalSnackBarEventController
import com.example.design_system.snackbar.rememberSnackBarEventController
import com.example.design_system.theme.MviSampleAppTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CircuitActivity : ComponentActivity() {
    @Inject
    @CircuitModule.MainCircuit
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val backStack = rememberSaveableBackStack(listOf(MainScreen))

            val mainNavigator = rememberAndroidScreenAwareNavigator(
                rememberCircuitNavigator(backStack),
                this@CircuitActivity,
            )

            val snackBarHostState = remember { SnackbarHostState() }

            val snackBarEventController = rememberSnackBarEventController()

            LaunchedEffect(Unit) {
                snackBarEventController.event.collect { event ->
                    snackBarHostState.showSnackbar(message = event.message)
                }
            }

            CompositionLocalProvider(
                LocalSnackBarEventController provides snackBarEventController
            ) {
                MviSampleAppTheme {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                    ) { paddingValues ->
                        CircuitCompositionLocals(circuit = circuit) {
                            NavigableCircuitContent(
                                modifier = Modifier.padding(paddingValues),
                                backStack = backStack,
                                navigator = mainNavigator,
                            )
                        }
                    }

                    BackHandler {
                        finish()
                    }
                }
            }
        }
    }
}
