package com.retroblade.achievo.data.repository

import com.retroblade.achievo.data.service.TokenService
import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.models.domain.TokenPair
import com.retroblade.achievo.models.domain.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenService: TokenService
): AuthRepository {

    private var refreshTokens: MutableMap<String, String> = mutableMapOf()
    private var users: MutableList<User> = mutableListOf(
        User(
            id = "test123",
            email = "test@test.rr",
            passwordHash = "h7GcBMyPtbX5yMe2wBiMIX2evoLlSl3JMkpglmqCqwc=",
        )
    )

    override suspend fun getUserByCredentials(email: String, passwordHash: String): User? {
        return users.firstOrNull { it.email == email && it.passwordHash == passwordHash }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun getUserById(id: String): User? {
        return users.firstOrNull { it.id == id }
    }

    override suspend fun registerUser(user: User): User {
        users.add(user)

        return user
    }

    override suspend fun createTokenPairForUserId(userId: String): TokenPair {
        val accessToken = tokenService.createAccessToken(userId)
        val refreshToken = tokenService.createRefreshToken()

        refreshTokens[userId] = refreshToken

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun saveRefreshTokenForUserId(userId: String, refreshToken: String) {
        refreshTokens[userId] = refreshToken
    }

    override suspend fun getRefreshTokenForUserId(userId: String): String? {
        return refreshTokens[userId]
    }

    override suspend fun validateRefreshTokenForUserId(userId: String): Boolean {
        val token = refreshTokens[userId] ?: return false

        return tokenService.validateRefreshToken(token)
    }

    override suspend fun verifyAccessToken(token: String): String? {
        return tokenService.verifyJWTToken(token)?.subject
    }
}