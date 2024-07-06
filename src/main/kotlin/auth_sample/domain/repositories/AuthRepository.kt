package auth_sample.domain.repositories

import auth_sample.models.domain.TokenPair
import auth_sample.models.domain.User

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