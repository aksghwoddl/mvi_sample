package com.example.presenter.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(
    modifier: Modifier,
    input: String,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (String) -> Unit,
) {
    Box(modifier = modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        OutlinedTextField(
            modifier = modifier.align(Alignment.Center),
            placeholder = {
                Text(text = hint)
            },
            value = input,
            onValueChange = {
                onTextChanged(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

@Preview
@Composable
fun UserInputFieldPreview() {
    val input by rememberSaveable {
        mutableStateOf("")
    }
    Column {
        UserInputField(modifier = Modifier, input = input, hint = "이름") {
        }
        UserInputField(modifier = Modifier, input = input, hint = "나이") {
        }
    }
}
