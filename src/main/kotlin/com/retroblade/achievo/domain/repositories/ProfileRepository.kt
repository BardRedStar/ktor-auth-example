package com.retroblade.achievo.domain.repositories

import com.retroblade.achievo.models.domain.Profile

interface ProfileRepository {
    suspend fun addProfile(profile: Profile)
    suspend fun getProfile(userId: String): Profile?
}