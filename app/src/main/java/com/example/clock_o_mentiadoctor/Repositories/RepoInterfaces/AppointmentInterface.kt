package com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces

import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.ResponseWrapper

interface AppointmentInterface {
    suspend fun getAppointment(accessToken:String,doctorId:String) : ResponseWrapper<AppointmentsModel>

    suspend fun updateAppointmentStatus(accessToken:String,doctorId:String,id: String,appointment: Appointment) : ResponseWrapper<Appointment>
}