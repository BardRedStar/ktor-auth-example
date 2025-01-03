package com.retroblade.achievo.service

import com.retroblade.achievo.models.domain.Profile
import com.retroblade.achievo.models.domain.RegisterUser
import com.retroblade.achievo.models.domain.User
import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.models.view.response.TokenResponse
import com.retroblade.achievo.repository.AuthRepository
import com.retroblade.achievo.repository.ProfileRepository
import com.retroblade.achievo.repository.TokenRepository
import com.retroblade.achievo.utils.CryptoHelper
import io.ktor.http.*
import javax.inject.Inject

interface AuthService {

    suspend fun registerUser(user: RegisterUser): MethodResult<TokenResponse>

    suspend fun loginUser(email: String, password: String): MethodResult<TokenResponse>

    suspend fun refreshToken(accessToken: String): MethodResult<TokenResponse>
}

class AuthServiceImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper,
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val profileRepository: ProfileRepository,
): AuthService {

    override suspend fun registerUser(user: RegisterUser): MethodResult<TokenResponse> {
        val existingUser = authRepository.getUserByEmail(user.email)
        if (existingUser != null) {
            return MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "User already exists for this email")
        }

        val registeredUser = authRepository.registerUser(
            User(
                id = user.userName,
                email = user.email,
                passwordHash = cryptoHelper.hashPassword(user.password)
            )
        )

        if (registeredUser == null) {
            return MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "Something went wrong")
        }

        val profile = Profile(
            id = registeredUser.id,
            email = registeredUser.email,
            firstName = user.firstName,
            lastName = user.lastName
        )
        profileRepository.addProfile(profile)

        val token = tokenRepository.createTokenPairForUserId(userId = registeredUser.id)
        return MethodResult.success(
            TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
        )
    }

    override suspend fun loginUser(email: String, password: String): MethodResult<TokenResponse> {
        val passwordHash = cryptoHelper.hashPassword(password)
        val user = authRepository.getUserByCredentials(email, passwordHash)

        return if (user != null) {
            val token = tokenRepository.createTokenPairForUserId(userId = user.id)
            MethodResult.success(TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken))
        } else {
            MethodResult.error(httpCode = HttpStatusCode.NotFound, message = "User not found for such credentials")
        }
    }

    override suspend fun refreshToken(accessToken: String): MethodResult<TokenResponse> {
        val userId = tokenRepository.verifyAccessToken(accessToken)

        if (userId == null) {
            return MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "Access token is invalid")
        }

        val isRefreshTokenValid = tokenRepository.validateRefreshTokenForUserId(userId = userId)

        if (!isRefreshTokenValid) {
            return MethodResult.error(HttpStatusCode.BadRequest, message = "Refresh token was not found, expired or malformed")
        }

        val token = tokenRepository.createTokenPairForUserId(userId = userId)
        return MethodResult.success(
            TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
        )
    }
}