package com.retroblade.achievo.service

import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.models.view.response.ProfileResponse
import com.retroblade.achievo.repository.ProfileRepository
import io.ktor.http.*
import javax.inject.Inject

interface ProfileService {
    suspend fun getProfile(userId: String): MethodResult<ProfileResponse>
}

class ProfileServiceImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): ProfileService {

    override suspend fun getProfile(userId: String): MethodResult<ProfileResponse> {
        val profile = profileRepository.getProfile(userId)

        return if (profile != null) {
            val response = ProfileResponse(
                id = profile.id,
                firstName = profile.firstName,
                lastName = profile.lastName,
                email = profile.email
            )
            MethodResult.success(response)
        } else {
            MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "User not found")
        }
    }
}