package com.example.mvisampleapp.ui.circuit.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.feature.main.components.UserInputField
import com.slack.circuitx.effects.LaunchedImpressionEffect

@Composable
fun Main(
    state: MainScreen.State,
    modifier: Modifier,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedImpressionEffect (state.mainModel.alertMessage) {
        if (state.mainModel.alertMessage.isNotEmpty()) {
            snackBarHostState.showSnackbar(message = state.mainModel.alertMessage)
            state.eventSink(MainScreen.State.MainScreenEvent.OnShowSnackBar)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                UserInputField(
                    modifier = modifier,
                    input = state.mainModel.name,
                    hint = "이름",
                ) { name ->
                    state.eventSink(MainScreen.State.MainScreenEvent.OnSetUserName(name))
                }
                UserInputField(
                    modifier = modifier,
                    input = state.mainModel.age,
                    hint = "나이",
                    keyboardType = KeyboardType.Number,
                ) { age ->
                    state.eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age))
                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    modifier = modifier,
                ) {
                    FunctionButton(
                        text = "저장하기",
                        modifier = modifier,
                    ) {
                        keyboardController?.hide()
                        state.eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)
                    }
                    Spacer(modifier = modifier.width(20.dp))
                    FunctionButton(
                        text = "목록",
                        modifier = modifier,
                    ) {
                        state.eventSink(MainScreen.State.MainScreenEvent.OnClickListButton)
                    }
                }
            }
        },
    )
}
