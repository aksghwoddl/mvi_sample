package com.example.presenter.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CommonDialog(
    modifier: Modifier,
    icon: ImageVector,
    dialogTitle: String,
    dialogText: String,
    confirmButtonText: String = "확인",
    cancelButtonText: String = "취소 ",
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(icon, contentDescription = null)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = onCancelClick,
        confirmButton = {
            Button(onClick = onConfirmClick) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onCancelClick) {
                Text(text = cancelButtonText)
            }
        },
    )
}
