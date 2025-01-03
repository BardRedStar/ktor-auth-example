package com.retroblade.achievo.utils

import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

interface CryptoHelper {
    fun hashPassword(password: String): String
    fun hashString(string: String): String

    fun base64EncodeString(string: String): String
    fun base64DecodeString(string: String): String
}

class CryptoHelperImpl @Inject constructor(): CryptoHelper {

    private val hasher = MessageDigest.getInstance("SHA-256")

    private val base64Encoder = Base64.getEncoder()
    private val base64Decoder = Base64.getDecoder()

    private val salt = "sad576f!#@as?"

    override fun hashPassword(password: String): String {
        val saltedPassword = password + salt
        return base64Encoder.encodeToString(hasher.digest(saltedPassword.toByteArray()))
    }

    override fun hashString(string: String): String {
        return hasher.digest(string.toByteArray()).toString(Charsets.UTF_8)
    }

    override fun base64EncodeString(string: String): String {
        return base64Encoder.encodeToString(string.toByteArray())
    }

    override fun base64DecodeString(string: String): String {
        return base64Decoder.decode(string).toString(Charsets.UTF_8)
    }
}