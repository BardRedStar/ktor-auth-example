package com.retroblade.achievo.domain.usecases.auth

import com.retroblade.achievo.domain.repositories.AuthRepository
import javax.inject.Inject

interface VerifyAccessTokenUseCase {
    suspend operator fun invoke(token: String): String?
}

class VerifyAccessTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : VerifyAccessTokenUseCase {

    override suspend fun invoke(token: String): String? {
        return authRepository.verifyAccessToken(token)
    }
}