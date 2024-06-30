package auth_sample.models.view.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)