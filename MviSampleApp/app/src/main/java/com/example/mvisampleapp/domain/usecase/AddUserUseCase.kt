package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.db.entity.UserEntity
import com.example.mvisampleapp.data.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        name: String,
        age: Int,
    ) {
        userRepository.addUser(
            userEntity = UserEntity(
                name = name,
                age = age,
            )
        )
    }
}
