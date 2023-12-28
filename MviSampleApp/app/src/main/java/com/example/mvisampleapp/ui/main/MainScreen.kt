package com.example.mvisampleapp.ui.main

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.mvisampleapp.ui.NavDestination
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.main.components.UserInputField
import com.example.mvisampleapp.ui.main.model.MainScreenElements
import com.example.mvisampleapp.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.launch

private const val TAG = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainViewModel,
    navController: NavController,
) {
    val state = viewModel.state.collectAsState()
    val snackBarHostState = SnackbarHostState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.run {
            effect.flowWithLifecycle(lifecycleOwner.lifecycle).collect { effect ->
                when (effect) {
                    is MainScreenElements.MainScreenEffect.ShowSnackBar -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(effect.message)
                        }
                    }

                    is MainScreenElements.MainScreenEffect.MoveListScreen -> {
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
                    modifier = modifier,
                    input = state.value.name,
                    hint = "이름",
                ) { name ->
                    viewModel.handleEvent(MainScreenElements.MainScreenEvent.SetUserName(name))
                }
                UserInputField(
                    modifier = modifier,
                    input = state.value.age,
                    hint = "나이",
                    keyboardType = KeyboardType.Number,
                ) { age ->
                    viewModel.handleEvent(MainScreenElements.MainScreenEvent.SetUserAge(age))
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
                        state.value.run {
                            if (name.isNotEmpty() && age.isNotEmpty()) {
                                viewModel.handleEvent(MainScreenElements.MainScreenEvent.ClickAddUserButton)
                                scope.launch {
                                    snackBarHostState.showSnackbar("정상적으로 저장되었습니다!")
                                }
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
                        viewModel.handleEvent(MainScreenElements.MainScreenEvent.ClickListButton)
                    }
                }
            }
        },
    )
}
