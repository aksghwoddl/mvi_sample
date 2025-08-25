package com.example.circuitapp.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.circuitapp.main.model.MainModel
import com.example.circuitapp.main.screen.MainScreen
import com.example.design_system.components.FunctionButton
import com.example.design_system.theme.MviSampleAppTheme
import com.example.presenter.feature.main.UserInputField

@Composable
fun Main(
    state: MainScreen.State,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserInputField(
            input = state.mainModel.name,
            hint = "이름",
        ) { name ->
            state.eventSink(MainScreen.State.MainScreenEvent.OnSetUserName(name))
        }
        UserInputField(
            input = state.mainModel.age,
            hint = "나이",
            keyboardType = KeyboardType.Number,
        ) { age ->
            state.eventSink(MainScreen.State.MainScreenEvent.OnSetUserAge(age))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FunctionButton(
                text = "저장하기",
            ) {
                keyboardController?.hide()
                state.eventSink(MainScreen.State.MainScreenEvent.OnClickAddUserButton)
            }
            FunctionButton(
                text = "목록",
            ) {
                state.eventSink(MainScreen.State.MainScreenEvent.OnClickListButton)
            }
        }
    }
}

@Preview
@Composable
private fun MainUiPreview() {
    MviSampleAppTheme {
        Main(
            state = MainScreen.State(mainModel = MainModel.placeHolder),
        )
    }
}
