package com.example.clock_o_mentiadoctor.models.profile

data class ProfileSetupResponse(
    val address: String,
    val age: Int,
    val certificate: String,
    val doctorId: String,
    val email: String,
    val gender: String,
    val id: String,
    val isVerified: Boolean,
    val name: String,
    val phone: String,
    val profileImage: String,
    val rating: Double
)