package com.retroblade.achievo.models.domain

data class User(
    val id: String,
    val email: String,
    val passwordHash: String
)