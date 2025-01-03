package com.retroblade.achievo.domain.usecases.auth

import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.domain.repositories.ProfileRepository
import com.retroblade.achievo.models.domain.Profile
import com.retroblade.achievo.models.domain.RegisterUser
import com.retroblade.achievo.models.domain.User
import com.retroblade.achievo.utils.CryptoHelper
import javax.inject.Inject

interface RegisterUserUseCase {
    suspend operator fun invoke(user: RegisterUser): String?
}

class RegisterUserUseCaseImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : RegisterUserUseCase {

    override suspend fun invoke(user: RegisterUser): String? {
        val passwordHash = cryptoHelper.hashPassword(user.password)

        val newUser = authRepository.registerUser(
            User(
                id = user.userName,
                email = user.email,
                passwordHash = passwordHash
            )
        )

        val profile = Profile(
            id = newUser.id,
            email = newUser.email,
            firstName = user.firstName,
            lastName = user.lastName
        )

        profileRepository.addProfile(profile)

        return newUser.id
    }
}