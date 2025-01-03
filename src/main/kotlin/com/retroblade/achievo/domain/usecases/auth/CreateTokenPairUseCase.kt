package com.retroblade.achievo.domain.usecases.auth

import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.models.domain.TokenPair
import javax.inject.Inject

interface CreateTokenPairUseCase {
    suspend operator fun invoke(userId: String): TokenPair
}

class CreateTokenPairUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : CreateTokenPairUseCase {

    override suspend fun invoke(userId: String): TokenPair {
        return authRepository.createTokenPairForUserId(userId)
    }
}