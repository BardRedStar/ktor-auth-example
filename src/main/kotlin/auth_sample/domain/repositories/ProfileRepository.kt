package auth_sample.domain.repositories

import auth_sample.models.domain.Profile

interface ProfileRepository {
    suspend fun addProfile(profile: Profile)
    suspend fun getProfile(userId: String): Profile?
}