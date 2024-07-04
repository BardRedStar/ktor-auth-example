package auth_sample.models.domain

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)