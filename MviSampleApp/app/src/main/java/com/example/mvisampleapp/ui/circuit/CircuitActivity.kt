package com.example.mvisampleapp.ui.circuit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import com.example.mvisampleapp.di.CircuitModule
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
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

            CircuitCompositionLocals(circuit = circuit) {
                NavigableCircuitContent(
                    backStack = backStack,
                    navigator = mainNavigator,
                )
            }

            BackHandler {
                finish()
            }
        }
    }
}
