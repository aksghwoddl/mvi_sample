package com.example.mvisampleapp.ui.feature.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mvisampleapp.data.model.entity.User

@Composable
fun UserListColumn(
    modifier: Modifier,
    list: List<User>,
    onClick: (User) -> Unit,
) {
    LazyColumn(content = {
        items(list) { user ->
            Card(
                modifier = modifier
                    .clickable {
                        onClick(user)
                    },
            ) {
                Row(
                    modifier = modifier.padding(10.dp),
                ) {
                    Text(text = "이름 : ${user.name}")
                    Spacer(
                        modifier = modifier.width(20.dp),
                    )
                    Text(text = "나이 : ${user.age}")
                }
            }
            Spacer(modifier = modifier.height(10.dp))
        }
    })
}
