package com.retroblade.achievo.repository

import com.retroblade.achievo.models.domain.User
import javax.inject.Inject

interface AuthRepository {

    suspend fun registerUser(user: User): User?

    suspend fun getUserByCredentials(email: String, passwordHash: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserById(id: String): User?
}

class AuthRepositoryImpl @Inject constructor(): AuthRepository{

    private var users: MutableList<User> = mutableListOf(
        User(
            id = "test123",
            email = "test@test.rr",
            passwordHash = "h7GcBMyPtbX5yMe2wBiMIX2evoLlSl3JMkpglmqCqwc=",
        )
    )

    override suspend fun registerUser(user: User): User {
        users.add(user)
        return user
    }

    override suspend fun getUserByCredentials(email: String, passwordHash: String): User? {
        return users.firstOrNull { it.email == email && it.passwordHash == passwordHash }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun getUserById(id: String): User? {
        return users.firstOrNull { it.id == id }
    }
}