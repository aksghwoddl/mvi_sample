package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(name: String, age: Int) {
        userRepository.deleteUser(
            name = name,
            age = age,
        )
    }
}
