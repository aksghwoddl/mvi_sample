package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.repository.UserRepository
import com.example.mvisampleapp.domain.mapper.toUserModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserListFlowUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.getAllUser().map {
        it.map { userEntity ->
            userEntity.toUserModel()
        }
    }
}