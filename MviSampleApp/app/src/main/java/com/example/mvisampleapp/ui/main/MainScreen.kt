package com.example.mvisampleapp.ui.main

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mvisampleapp.ui.NavDestination
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.main.components.UserInputField
import com.example.mvisampleapp.ui.main.model.MainScreenElements
import com.example.mvisampleapp.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "MainScreen"

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = SnackbarHostState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.run {
            effect.collectLatest { effect ->
                when (effect) {
                    is MainScreenElements.MainScreenEffect.ShowSnackBar -> { // SnackBar 표시
                        snackBarHostState.showSnackbar(effect.message)
                    }

                    is MainScreenElements.MainScreenEffect.MoveListScreen -> { // ListScreen으로 랜딩
                        navController.navigate(NavDestination.LIST)
                    }
                }
            }
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
                    modifier = Modifier,
                    input = state.value.name,
                    hint = "이름",
                ) { name ->
                    viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnSetUserName(name))
                }
                UserInputField(
                    modifier = Modifier,
                    input = state.value.age,
                    hint = "나이",
                    keyboardType = KeyboardType.Number,
                ) { age ->
                    viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnSetUserAge(age))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier,
                ) {
                    FunctionButton(
                        text = "저장하기",
                        modifier = Modifier,
                    ) {
                        keyboardController?.hide()
                        viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnClickAddUserButton)
                    }
                    Spacer(modifier = modifier.width(20.dp))
                    FunctionButton(
                        text = "목록",
                        modifier = modifier,
                    ) {
                        viewModel.handleEvent(MainScreenElements.MainScreenEvent.OnClickListButton)
                    }
                }
            }
        },
    )
}
