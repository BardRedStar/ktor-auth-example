package auth_sample.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenPayload(
    val expiration: Long,
    val checksum: String
)