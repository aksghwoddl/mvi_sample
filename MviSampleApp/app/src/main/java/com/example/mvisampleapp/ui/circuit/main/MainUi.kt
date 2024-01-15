package com.example.mvisampleapp.ui.circuit.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mvisampleapp.ui.circuit.main.screen.MainScreen
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.main.components.UserInputField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Main(
    state: MainScreen.State,
    modifier: Modifier,
) {
    val snackBarHostState = SnackbarHostState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

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
                    state.eventSink(MainScreen.State.MainScreenEvent.SetUserName(name))
                }
                UserInputField(
                    modifier = modifier,
                    input = state.mainModel.age,
                    hint = "나이",
                    keyboardType = KeyboardType.Number,
                ) { age ->
                    state.eventSink(MainScreen.State.MainScreenEvent.SetUserAge(age))
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
                        state.mainModel.run {
                            if (name.isNotEmpty() && age.isNotEmpty()) {
                                state.eventSink(MainScreen.State.MainScreenEvent.ClickAddUserButton)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("깂을 확인해주세요!")
                                }
                            }
                        }
                    }
                    Spacer(modifier = modifier.width(20.dp))
                    FunctionButton(
                        text = "목록",
                        modifier = modifier,
                    ) {
                        state.eventSink(MainScreen.State.MainScreenEvent.ClickListButton)
                    }
                }
            }
        },
    )
}
