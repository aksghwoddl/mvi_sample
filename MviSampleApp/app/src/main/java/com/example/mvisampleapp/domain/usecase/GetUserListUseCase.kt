package com.example.mvisampleapp.domain.usecase

import com.example.mvisampleapp.data.repository.UserRepository
import com.example.mvisampleapp.domain.mapper.toUserModel
import com.example.mvisampleapp.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<List<UserModel>> {
        return userRepository.getAllUser().map {
            it.map { userEntity ->
                userEntity.toUserModel()
            }
        }
    }
}
