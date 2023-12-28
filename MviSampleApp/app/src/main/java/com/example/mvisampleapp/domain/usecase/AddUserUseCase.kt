package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.model.entity.User
import com.example.mvisampleapp.data.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User) {
        userRepository.addUser(user)
    }
}
