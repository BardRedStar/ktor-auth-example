package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.TokenPair
import auth_sample.utils.TokenHelper
import javax.inject.Inject

interface CreateTokenPairUseCase {
    suspend operator fun invoke(userId: String): TokenPair
}

class CreateTokenPairUseCaseImpl @Inject constructor(
    private val tokenHelper: TokenHelper,
    private val authRepository: AuthRepository
) : CreateTokenPairUseCase {

    override suspend fun invoke(userId: String): TokenPair {
        val accessToken = tokenHelper.createAccessToken(userId)

        val refreshToken = tokenHelper.createRefreshToken()
        authRepository.saveRefreshTokenForUserId(userId = userId, refreshToken = refreshToken)

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }
}