package com.retroblade.achievo.domain.usecases.auth

import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.models.domain.User
import javax.inject.Inject

interface GetUserByEmailUseCase {
    suspend operator fun invoke(email: String): User?
}

class GetUserByEmailUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : GetUserByEmailUseCase {

    override suspend fun invoke(email: String): User? {
        return authRepository.getUserByEmail(email)
    }
}