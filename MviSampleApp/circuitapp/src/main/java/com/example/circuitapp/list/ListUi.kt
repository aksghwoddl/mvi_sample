package com.example.circuitapp.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val eventSink by rememberUpdatedState(state.eventSink)
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserListColumn(
            list = state.listModel.userList,
        ) { item ->
            eventSink(ListScreen.State.ListScreenEvent.OnClickUserItem(item))
            eventSink(ListScreen.State.ListScreenEvent.ShowUserDeleteDialog)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FunctionButton(
                text = "전체삭제",
            ) {
                eventSink(ListScreen.State.ListScreenEvent.OnClickDeleteAllButton)
            }
            FunctionButton(
                text = "이전화면",
            ) {
                eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
            }
        }
    }

    BackHandler {
        eventSink(ListScreen.State.ListScreenEvent.OnClickPreviousButton)
    }

    if (state.listModel.isShowUserDeleteDialog) {
        CommonDialog(
            icon = Icons.Default.Delete,
            dialogTitle = "삭제",
            dialogText = "정말 삭제 하시겠습니까?",
            onConfirmClick = {
                state.listModel.selectedUser?.let { user ->
                    eventSink(
                        ListScreen.State.ListScreenEvent.OnClickDeleteButton(
                            name = user.name,
                            age = user.age
                        )
                    )
                }
            },
            onCancelClick = {
                eventSink(ListScreen.State.ListScreenEvent.DismissUserDeleteDialog)
            },
        )
    }

    if (state.listModel.isShowDeleteAllUserDialog) {
        CommonDialog(
            icon = Icons.Default.Delete,
            dialogTitle = "전체삭제",
            dialogText = "모든 유저를 삭제하시겠습니까?",
            onConfirmClick = {
                eventSink(ListScreen.State.ListScreenEvent.OnClickDeleteAllConfirmButton)
            },
            onCancelClick = {
                eventSink(ListScreen.State.ListScreenEvent.OnClickDeleteAllCancelButton)
            },
        )
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
