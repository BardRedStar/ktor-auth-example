package auth_sample.data.repository

import auth_sample.domain.repositories.AuthRepository
import auth_sample.models.domain.RegisterUser
import auth_sample.models.domain.User
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(): AuthRepository {

    private var refreshTokens: MutableList<String> = mutableListOf()
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
}