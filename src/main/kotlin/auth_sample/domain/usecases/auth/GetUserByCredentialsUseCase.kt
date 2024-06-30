package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.User
import auth_sample.utils.CryptoHelper
import javax.inject.Inject

interface GetUserByCredentialsUseCase {
    suspend operator fun invoke(email: String, password: String): User?
}

class GetUserByCredentialsUseCaseImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper,
    private val authRepository: AuthRepository
) : GetUserByCredentialsUseCase {

    override suspend fun invoke(email: String, password: String): User? {
        val passwordHash = cryptoHelper.hashPassword(password)
        return authRepository.getUserByCredentials(email, passwordHash)
    }
}