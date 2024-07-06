package auth_sample.data.repository

import auth_sample.data.service.TokenService
import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.TokenPair
import auth_sample.models.domain.User
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenService: TokenService
): AuthRepository {

    private var refreshTokens: MutableMap<String, String> = mutableMapOf()
    private var users: MutableList<User> = mutableListOf()

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
        val newUser = user.copy(id = UUID.randomUUID().toString())
        users.add(newUser)

        return newUser
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