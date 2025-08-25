package com.example.domain.usecase

import com.example.data.repository.UserRepository
import javax.inject.Inject

class DeleteAllUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.deleteAll()
    }
}