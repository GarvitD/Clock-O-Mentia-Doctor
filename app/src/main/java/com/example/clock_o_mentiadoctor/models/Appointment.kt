package com.example.clock_o_mentiadoctor.models

data class Appointment(
    val date: String,
    val detailedInfo: String,
    val doctorEmail: String,
    val doctorName: String,
    val doctorProfileUrl: String,
    val id: String,
    val meetingId: String,
    val name: String,
    val patientId: String,
    val reportUrl: String,
    val status: Int
)