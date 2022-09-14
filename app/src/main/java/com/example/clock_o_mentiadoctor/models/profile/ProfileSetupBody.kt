package com.example.clock_o_mentiadoctor.models.profile

data class ProfileSetupBody(
    val address: String?,
    val age: String?,
    val certificate: String?,
    val doctorId: String?,
    val email: String?,
    val gender: String?,
    val name: String?,
    val phone: String?,
    val profileImage: String?
)