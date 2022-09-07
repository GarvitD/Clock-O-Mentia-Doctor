package com.example.clock_o_mentiadoctor.models.authentication

data class User(
    val age: Int,
    val email: String,
    val id: String,
    val loginType: String,
    val name: String,
    val role: String
)