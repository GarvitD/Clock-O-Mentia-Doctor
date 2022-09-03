package com.example.clock_o_mentiadoctor.models.authentication

data class AuthResponse(
    val tokens: Tokens,
    val user: User
)