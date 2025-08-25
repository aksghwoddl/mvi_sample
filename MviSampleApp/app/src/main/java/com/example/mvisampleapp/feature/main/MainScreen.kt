package com.example.mvisampleapp.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.design_system.components.FunctionButton
import com.example.design_system.theme.MviSampleAppTheme
import com.example.mvisampleapp.NavDestination
import com.example.mvisampleapp.feature.main.model.MainScreenElements
import com.example.mvisampleapp.feature.main.viewmodel.MainViewModel
import com.example.presenter.feature.main.UserInputField
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "MainScreen"

@Composable
fun MainRoute(
    navController: NavController,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.run {
            effect.collectLatest { effect ->
                when (effect) {
                    is MainScreenElements.MainScreenEffect.ShowSnackBar -> { // SnackBar 표시
                        onShowSnackBar(effect.message)
                    }

                    is MainScreenElements.MainScreenEffect.MoveListScreen -> { // ListScreen으로 랜딩
                        navController.navigate(NavDestination.LIST)
                    }
                }
            }
        }
    }

    MainScreen(
        modifier = modifier,
        state = state,
        onSetUserAge = { age ->
            viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnSetUserAge(age))
        },
        onSetUserName = { name ->
            viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnSetUserName(name))
        },
        onClickListButton = {
            viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnClickListButton)
        },
        onClickSaveButton = {
            keyboardController?.hide()
            viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnClickAddUserButton)
        }
    )
}

@Composable
internal fun MainScreen(
    state: MainScreenElements.MainScreenUiState,
    onSetUserName: (String) -> Unit,
    onSetUserAge: (String) -> Unit,
    onClickSaveButton: () -> Unit,
    onClickListButton: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserInputField(
            modifier = Modifier,
            input = state.name,
            hint = "이름",
            onTextChanged = onSetUserName
        )
        UserInputField(
            modifier = Modifier,
            input = state.age,
            hint = "나이",
            keyboardType = KeyboardType.Number,
            onTextChanged = onSetUserAge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FunctionButton(
                text = "저장하기",
                modifier = Modifier,
                onClick = onClickSaveButton
            )
            FunctionButton(
                text = "목록",
                modifier = modifier,
                onClick = onClickListButton
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MviSampleAppTheme {
        MainScreen(
            state = MainScreenElements.MainScreenUiState(),
            onSetUserName = {},
            onSetUserAge = {},
            onClickListButton = {},
            onClickSaveButton = {},
        )
    }
}
