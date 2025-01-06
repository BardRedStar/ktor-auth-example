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
import com.retroblade.achievo.utils.TokenUtils
import io.ktor.http.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface AuthService {

    suspend fun registerUser(user: RegisterUser): MethodResult<TokenResponse>

    suspend fun loginUser(email: String, password: String): MethodResult<TokenResponse>

    suspend fun refreshToken(refreshToken: String): MethodResult<TokenResponse>
}

class AuthServiceImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper,
    private val tokenUtils: TokenUtils,
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val profileRepository: ProfileRepository,
): AuthService {

    private val logger = LoggerFactory.getLogger(AuthServiceImpl::class.java)

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

    override suspend fun refreshToken(refreshToken: String): MethodResult<TokenResponse> {
        if (tokenRepository.validateRefreshToken(refreshToken)) {
            return MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "Refresh token is invalid")
        }
        val userId = tokenUtils.getUserIdFromToken(refreshToken)

        if(userId == null){
            logger.error("Wrong data in token: token $refreshToken")
            return MethodResult.error(httpCode = HttpStatusCode.BadRequest, message = "Something went wrong")
        }

        val token = tokenRepository.createTokenPairForUserId(userId = userId)
        return MethodResult.success(
            TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
        )
    }
}