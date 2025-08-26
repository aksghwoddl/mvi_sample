package com.example.mvisampleapp.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.design_system.components.FunctionButton
import com.example.design_system.dialog.CommonDialog
import com.example.design_system.theme.MviSampleAppTheme
import com.example.mvisampleapp.feature.list.model.ListScreenElements
import com.example.mvisampleapp.feature.list.viewmodel.ListViewModel
import com.example.presenter.feature.list.UserListColumn
import com.example.presenter.feature.list.model.User
import kotlinx.collections.immutable.persistentListOf
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

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListScreenElements.ListScreenEffect.ShowSnackBar -> {
                    onShowSnackBar(effect.message)
                }

                is ListScreenElements.ListScreenEffect.MoveMainScreen -> {
                    navController.navigateUp()
                }
            }
        }
    }

    if (state.isShowUserDeleteDialog) {
        CommonDialog(
            icon = Icons.Default.Delete,
            dialogTitle = "삭제",
            dialogText = "정말 삭제 하시겠습니까?",
            onConfirmClick = {
                state.selectedUser?.let { user ->
                    viewModel.handleEvent(
                        ListScreenElements.ListScreenEvent.OnClickDeleteButton(
                            user
                        ),
                    )
                    viewModel.handleEvent(ListScreenElements.ListScreenEvent.DismissUserDeleteDialog)
                }
            },
            onCancelClick = {
                viewModel.handleEvent(ListScreenElements.ListScreenEvent.DismissUserDeleteDialog)
            },
        )
    }

    ListScreen(
        modifier = modifier,
        state = state,
        onClickUser = { user ->
            viewModel.handleEvent(ListScreenElements.ListScreenEvent.OnClickUserItem(user))
            viewModel.handleEvent(ListScreenElements.ListScreenEvent.ShowUserDeleteDialog)
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
            list = state.userList,
            onClick = onClickUser
        )

        FunctionButton(
            text = "이전화면",
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
                userList = persistentListOf(
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