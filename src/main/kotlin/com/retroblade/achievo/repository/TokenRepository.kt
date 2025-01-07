package com.retroblade.achievo.repository

import com.retroblade.achievo.models.domain.TokenPair
import com.retroblade.achievo.utils.TokenUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface TokenRepository {

    suspend fun saveRefreshToken(accessToken: String, refreshToken: String)

    suspend fun validateRefreshToken(accessToken: String, refreshToken: String): Boolean

    suspend fun verifyAccessToken(token: String): String?

    suspend fun createTokenPairForUserId(userId: String): TokenPair

    suspend fun removeRefreshToken(accessToken: String): Boolean
}

class TokenRepositoryImpl @Inject constructor(
    private val tokenUtils: TokenUtils,
): TokenRepository {

    private val logger = LoggerFactory.getLogger(TokenRepositoryImpl::class.java)

    private var refreshTokens: MutableMap<String, String> = mutableMapOf()

    override suspend fun saveRefreshToken(accessToken: String, refreshToken: String) {
        refreshTokens[accessToken] = refreshToken
    }

    override suspend fun validateRefreshToken(accessToken: String, refreshToken: String): Boolean {
        if(refreshTokens[accessToken] == null) {
            logger.error("Refresh token is not found in storage\nAccess token: $accessToken\nRefresh token: $refreshToken")
            return false
        }
        if(!tokenUtils.validateRefreshToken(refreshToken)){
            logger.error("Refresh token is not valid.\nAccess token: $accessToken\nRefresh token: $refreshToken")
            return false
        }
        return true
    }

    override suspend fun verifyAccessToken(token: String): String? {
        return tokenUtils.verifyJWTToken(token)?.subject
    }

    override suspend fun createTokenPairForUserId(userId: String): TokenPair {
        val accessToken = tokenUtils.createAccessToken(userId)
        val refreshToken = tokenUtils.createRefreshToken(userId)

        refreshTokens[accessToken] = refreshToken

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun removeRefreshToken(accessToken: String): Boolean {
        val result = refreshTokens.remove(accessToken) != null
        return result
    }
}