package com.retroblade.achievo.data.repository

import com.retroblade.achievo.domain.repositories.ProfileRepository
import com.retroblade.achievo.models.domain.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(): ProfileRepository {

    private val profiles: MutableList<Profile> = mutableListOf(
        Profile(
            id = "test123",
            email = "test@test.rr",
            firstName = "Test",
            lastName = "Pesp"
        )
    )

    override suspend fun addProfile(profile: Profile) {
        profiles.add(profile)
    }

    override suspend fun getProfile(userId: String): Profile? {
        return profiles.firstOrNull { it.id == userId }
    }
}