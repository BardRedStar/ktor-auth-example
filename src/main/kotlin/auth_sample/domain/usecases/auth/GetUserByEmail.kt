package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.User
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