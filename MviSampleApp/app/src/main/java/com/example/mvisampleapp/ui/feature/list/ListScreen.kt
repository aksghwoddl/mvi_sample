package com.example.mvisampleapp.ui.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mvisampleapp.ui.common.components.FunctionButton
import com.example.mvisampleapp.ui.common.dialog.CommonDialog
import com.example.mvisampleapp.ui.feature.list.components.UserListColumn
import com.example.mvisampleapp.ui.feature.list.model.ListScreenElements
import com.example.mvisampleapp.ui.feature.list.viewmodel.ListViewModel
import com.example.mvisampleapp.ui.model.User
import com.example.mvisampleapp.ui.theme.MviSampleAppTheme
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "ListScreen"

@Composable
fun ListRoute(
    navController: NavController,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListScreenElements.ListScreenEffect.ShowSnackBar -> {
                    onShowSnackBar(effect.message)
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
                state.selectedUser?.let { user ->
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

    ListScreen(
        state = state,
        onClickUser = { user ->
            viewModel.handleEvent(ListScreenElements.ListScreenEvent.OnClickUserItem(user))
        },
        onClickBackButton = {
            viewModel.handleEvent(ListScreenElements.ListScreenEvent.OnClickPreviousButton)
        }
    )
}

@Composable
internal fun ListScreen(
    state: ListScreenElements.ListScreenState,
    onClickUser: (User) -> Unit,
    onClickBackButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserListColumn(
            modifier = modifier,
            list = state.userList,
            onClick = onClickUser
        )

        FunctionButton(
            text = "이전화면",
            modifier = modifier,
            onClick = onClickBackButton
        )
    }
}


@Preview
@Composable
private fun ListScreenPreview() {
    MviSampleAppTheme {
        ListScreen(
            state = ListScreenElements.ListScreenState(
                userList = listOf(
                    User(
                        name = "테스트1.",
                        age = 23
                    ),
                    User(
                        name = "테스트2.",
                        age = 27
                    )
                )
            ),
            onClickUser = {},
            onClickBackButton = {},
        )
    }
}