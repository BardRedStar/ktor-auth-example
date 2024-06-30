package auth_sample.utils

import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

interface CryptoHelper {
    fun hashPassword(password: String): String
}

class CryptoHelperImpl @Inject constructor(): CryptoHelper {

    private val hasher = MessageDigest.getInstance("SHA-256")
    private val base64Encoder = Base64.getEncoder()
    private val salt = "sad576f!#@as?"

    override fun hashPassword(password: String): String {
        val saltedPassword = password + salt
        return base64Encoder.encodeToString(hasher.digest(saltedPassword.toByteArray()))
    }
}