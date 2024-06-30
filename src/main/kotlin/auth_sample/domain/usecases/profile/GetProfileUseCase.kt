package auth_sample.domain.usecases.profile

import auth_sample.domain.repositories.ProfileRepository
import auth_sample.models.domain.Profile
import javax.inject.Inject

interface GetProfileUseCase {
    suspend operator fun invoke(userId: String): Profile?
}

class GetProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {

    override suspend fun invoke(userId: String): Profile? {
        return profileRepository.getProfile(userId)
    }
}