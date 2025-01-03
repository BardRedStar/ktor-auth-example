package com.retroblade.achievo.domain.usecases.profile

import com.retroblade.achievo.domain.repositories.ProfileRepository
import com.retroblade.achievo.models.domain.Profile
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