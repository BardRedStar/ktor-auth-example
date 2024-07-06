package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import javax.inject.Inject

interface ValidateRefreshTokenUseCase {
    suspend operator fun invoke(userId: String): Boolean
}

class ValidateRefreshTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ValidateRefreshTokenUseCase {

    override suspend fun invoke(userId: String): Boolean {
        return authRepository.validateRefreshTokenForUserId(userId)
    }
}