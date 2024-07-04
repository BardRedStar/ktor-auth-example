package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.utils.TokenHelper
import javax.inject.Inject

interface ValidateRefreshTokenUseCase {
    suspend operator fun invoke(userId: String): Boolean
}

class ValidateRefreshTokenUseCaseImpl @Inject constructor(
    private val tokenHelper: TokenHelper,
    private val authRepository: AuthRepository
) : ValidateRefreshTokenUseCase {

    override suspend fun invoke(userId: String): Boolean {
        val refreshToken = authRepository.getRefreshTokenForUserId(userId = userId) ?: return false

        return tokenHelper.validateRefreshToken(refreshToken)
    }
}