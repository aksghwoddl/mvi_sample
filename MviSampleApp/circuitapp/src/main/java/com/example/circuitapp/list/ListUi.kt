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
import com.example.circuitapp.list.model.ListModel
import com.example.circuitapp.list.screen.ListScreen
import com.example.design_system.components.FunctionButton
import com.example.design_system.dialog.CommonDialog
import com.example.design_system.theme.MviSampleAppTheme
import com.example.presenter.feature.list.UserListColumn
import com.example.presenter.feature.list.model.User
import kotlinx.collections.immutable.persistentListOf

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
            list = state.listModel.userList,
        ) { item ->
            state.eventSink(ListScreen.State.ListScreenEvent.OnClickUserItem(item))
            showDeleteDialog = true
        }

        FunctionButton(
            text = "이전화면",
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
                    userList = persistentListOf(
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
