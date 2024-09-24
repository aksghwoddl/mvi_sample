package com.example.mvisampleapp.ui.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.common.dialog.CommonDialog
import com.example.mvisampleapp.ui.feature.list.components.UserListColumn
import com.example.mvisampleapp.ui.feature.list.model.ListScreenElements
import com.example.mvisampleapp.ui.feature.list.viewmodel.ListViewModel
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "ListScreen"

@Composable
fun ListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListScreenElements.ListScreenEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(effect.message)
                }

                is ListScreenElements.ListScreenEffect.MoveMainScreen -> {
                    navController.navigateUp()
                }

                is ListScreenElements.ListScreenEffect.ShowDeleteDialog -> {
                    showDeleteDialog = true
                }
            }
        }
    }

    if (showDeleteDialog) {
        CommonDialog(
            modifier = modifier,
            icon = Icons.Default.Delete,
            dialogTitle = "삭제",
            dialogText = "유저를 삭제 하시겠습니까?",
            onConfirmClick = {
                state.value.selectedUser?.let { user ->
                    viewModel.handleEvent(
                        ListScreenElements.ListScreenEvent.OnClickDeleteButton(user),
                    )
                    showDeleteDialog = false
                }
            },
            onCancelClick = {
                showDeleteDialog = false
            },
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                UserListColumn(
                    modifier = modifier,
                    list = state.value.userList,
                ) { item ->
                    viewModel.handleEvent(ListScreenElements.ListScreenEvent.OnClickUserItem(item))
                }

                FunctionButton(
                    text = "이전화면",
                    modifier = modifier,
                ) {
                    viewModel.handleEvent(ListScreenElements.ListScreenEvent.OnClickPreviousButton)
                }
            }
        },
    )
}
