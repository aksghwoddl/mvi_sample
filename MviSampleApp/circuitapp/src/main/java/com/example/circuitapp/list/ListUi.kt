package com.example.circuitapp.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvisampleapp.ui.circuit.list.model.ListModel
import com.example.mvisampleapp.ui.circuit.list.screen.ListScreen
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.common.dialog.CommonDialog
import com.example.mvisampleapp.ui.feature.list.components.UserListColumn
import com.example.mvisampleapp.ui.model.User
import com.example.mvisampleapp.ui.theme.MviSampleAppTheme

@Composable
fun List(
    state: ListScreen.State,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by rememberSaveable {
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
                    state.eventSink(
                        ListScreen.State.ListScreenEvent.OnClickDeleteButton(
                            name = user.name,
                            age = user.age
                        )
                    )
                    showDeleteDialog = false
                }
            },
            onCancelClick = {
                showDeleteDialog = false
            },
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserListColumn(
            modifier = modifier,
            list = state.listModel.userList,
        ) { item ->
            state.eventSink(ListScreen.State.ListScreenEvent.OnClickUserItem(item))
            showDeleteDialog = true
        }

        FunctionButton(
            text = "이전화면",
            modifier = modifier,
        ) {
            state.eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
        }
    }

    BackHandler {
        state.eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
    }
}

@Preview
@Composable
private fun ListUiPreview() {
    MviSampleAppTheme {
        List(
            state = ListScreen.State(
                listModel = ListModel(
                    userList = listOf(
                        User(
                            name = "테스트1",
                            age = 23,
                        ),
                        User(
                            name = "테스트2",
                            age = 27,
                        )
                    ),
                    selectedUser = null,
                )
            )
        )
    }
}
