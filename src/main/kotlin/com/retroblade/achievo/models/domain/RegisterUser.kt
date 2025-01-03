package com.retroblade.achievo.models.domain

data class RegisterUser(
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)