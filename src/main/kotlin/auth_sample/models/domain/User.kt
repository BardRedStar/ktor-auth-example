package auth_sample.models.domain

data class User(
    val id: String,
    val email: String,
    val passwordHash: String
)