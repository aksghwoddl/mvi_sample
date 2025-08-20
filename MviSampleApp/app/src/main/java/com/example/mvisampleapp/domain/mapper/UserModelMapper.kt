package com.example.mvisampleapp.domain.mapper

import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.domain.model.UserModel

fun UserEntity.toUserModel() = UserModel(
    name = name,
    age = age,
)