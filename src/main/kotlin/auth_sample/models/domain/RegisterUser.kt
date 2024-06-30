package auth_sample.models.domain

data class RegisterUser(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)