package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.TokenPair
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