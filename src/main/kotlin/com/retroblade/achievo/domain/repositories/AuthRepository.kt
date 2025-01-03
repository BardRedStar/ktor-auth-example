package com.retroblade.achievo.domain.repositories

import com.retroblade.achievo.models.domain.TokenPair
import com.retroblade.achievo.models.domain.User

interface AuthRepository {

    suspend fun createTokenPairForUserId(userId: String): TokenPair

    suspend fun getUserByCredentials(email: String, passwordHash: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: String): User?

    suspend fun registerUser(user: User): User

    suspend fun saveRefreshTokenForUserId(userId: String, refreshToken: String)

    suspend fun getRefreshTokenForUserId(userId: String): String?

    suspend fun validateRefreshTokenForUserId(userId: String): Boolean

    suspend fun verifyAccessToken(token: String): String?
}