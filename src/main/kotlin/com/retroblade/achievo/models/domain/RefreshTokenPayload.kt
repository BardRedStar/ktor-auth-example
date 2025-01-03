package com.retroblade.achievo.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenPayload(
    val expiration: Long,
    val checksum: String
)