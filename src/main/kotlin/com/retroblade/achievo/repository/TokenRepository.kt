package com.retroblade.achievo.repository

import com.retroblade.achievo.models.domain.TokenPair
import com.retroblade.achievo.utils.TokenUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface TokenRepository {

    suspend fun saveRefreshToken(token: String)

    suspend fun validateRefreshToken(refreshToken: String): Boolean

    suspend fun verifyAccessToken(token: String): String?

    suspend fun createTokenPairForUserId(userId: String): TokenPair
}

class TokenRepositoryImpl @Inject constructor(
    private val tokenUtils: TokenUtils,
): TokenRepository {

    private val logger = LoggerFactory.getLogger(TokenRepositoryImpl::class.java)

    private var refreshTokens: MutableSet<String> = mutableSetOf()

    override suspend fun saveRefreshToken(token: String) {
        refreshTokens.add(token)
    }

    override suspend fun validateRefreshToken(refreshToken: String): Boolean {
        return refreshTokens.contains(refreshToken) && tokenUtils.validateRefreshToken(refreshToken)
    }

    override suspend fun verifyAccessToken(token: String): String? {
        return tokenUtils.verifyJWTToken(token)?.subject
    }

    override suspend fun createTokenPairForUserId(userId: String): TokenPair {
        val accessToken = tokenUtils.createAccessToken(userId)
        val refreshToken = tokenUtils.createRefreshToken(userId)

        refreshTokens.add(refreshToken)

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }
}