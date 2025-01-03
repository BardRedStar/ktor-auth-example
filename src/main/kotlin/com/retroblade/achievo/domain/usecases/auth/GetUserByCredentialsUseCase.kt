package com.retroblade.achievo.domain.usecases.auth

import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.models.domain.User
import com.retroblade.achievo.utils.CryptoHelper
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