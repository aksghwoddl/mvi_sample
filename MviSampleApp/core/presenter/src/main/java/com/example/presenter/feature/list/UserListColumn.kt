package com.example.presenter.feature.list

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
import com.example.presenter.feature.list.model.User
import kotlinx.collections.immutable.PersistentList

@Composable
fun UserListColumn(
    modifier: Modifier = Modifier,
    list: PersistentList<User>,
    onClick: (User) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(list) { user ->
                Card(
                    modifier = Modifier
                        .clickable {
                            onClick(user)
                        },
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Text(text = "이름 : ${user.name}")
                        Spacer(
                            modifier = Modifier.width(20.dp),
                        )
                        Text(text = "나이 : ${user.age}")
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        })
}
