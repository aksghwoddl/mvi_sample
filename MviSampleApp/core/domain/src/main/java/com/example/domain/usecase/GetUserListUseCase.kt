package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.domain.mapper.toUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        userRepository.getAllUser().firstOrNull()?.let {
            it.map { userEntity ->
                userEntity.toUserModel()
            }
        } ?: emptyList()
    }
}
