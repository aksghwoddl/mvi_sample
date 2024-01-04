package com.example.mvisampleapp.ui.circuit.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.common.dialog.CommonDialog
import com.example.mvisampleapp.ui.list.components.UserListColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List(
    state: ListScreen.State,
    modifier: Modifier,
) {
    val snackBarHostState = SnackbarHostState()
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (showDeleteDialog) {
        CommonDialog(
            modifier = modifier,
            icon = Icons.Default.Delete,
            dialogTitle = "삭제",
            dialogText = "유저를 삭제 하시겠습니까?",
            onConfirmClick = {
                state.listModel.selectedUser?.let { user ->
                    state.eventSink(ListScreen.State.ListScreenEvent.ClickDeleteButton(user))
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
                modifier = modifier.fillMaxSize().padding(paddingValues = paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                UserListColumn(
                    modifier = modifier,
                    list = state.listModel.userList,
                ) { item ->
                    state.eventSink(ListScreen.State.ListScreenEvent.ClickUserItem(item))
                    showDeleteDialog = true
                }

                FunctionButton(
                    text = "이전화면",
                    modifier = modifier,
                ) {
                    state.eventSink(ListScreen.State.ListScreenEvent.ClickPreviousButton)
                }
            }
        },
    )
}
