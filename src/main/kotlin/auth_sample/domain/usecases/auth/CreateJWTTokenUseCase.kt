package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.utils.JWTHelper
import javax.inject.Inject

interface CreateJWTTokenUseCase {
    suspend operator fun invoke(userId: String): String?
}

class CreateJWTTokenUseCaseImpl @Inject constructor(
    private val jwtHelper: JWTHelper,
    private val authRepository: AuthRepository
) : CreateJWTTokenUseCase {

    override suspend fun invoke(userId: String): String? {
        val token = jwtHelper.createToken(userId)

        return token
    }
}