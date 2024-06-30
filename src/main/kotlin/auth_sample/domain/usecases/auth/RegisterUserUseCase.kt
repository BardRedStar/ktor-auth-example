package auth_sample.domain.usecases.auth

import auth_sample.domain.repositories.AuthRepository
import auth_sample.domain.repositories.ProfileRepository
import auth_sample.models.domain.Profile
import auth_sample.models.domain.RegisterUser
import auth_sample.models.domain.User
import auth_sample.utils.CryptoHelper
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
                id = "",
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