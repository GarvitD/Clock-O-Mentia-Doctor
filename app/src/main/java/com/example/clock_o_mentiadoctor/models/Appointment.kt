package com.example.clock_o_mentiadoctor.models

import java.io.Serializable

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
    var status: Int
): Serializable