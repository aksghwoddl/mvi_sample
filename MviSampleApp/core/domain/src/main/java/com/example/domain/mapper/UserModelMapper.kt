package com.example.domain.mapper

import com.example.data.db.entity.UserEntity
import com.example.domain.model.UserModel

fun UserEntity.toUserModel() = UserModel(
    name = name,
    age = age,
)