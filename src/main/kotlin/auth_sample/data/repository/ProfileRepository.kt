package auth_sample.data.repository

import auth_sample.domain.repositories.ProfileRepository
import auth_sample.models.domain.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(): ProfileRepository {

    private val profiles: MutableList<Profile> = mutableListOf()

    override suspend fun addProfile(profile: Profile) {
        profiles.add(profile)
    }

    override suspend fun getProfile(userId: String): Profile? {
        return profiles.firstOrNull { it.id == userId }
    }
}