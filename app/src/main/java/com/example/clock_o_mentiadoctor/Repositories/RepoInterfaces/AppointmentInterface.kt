package com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces

import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.ResponseWrapper

interface AppointmentInterface {
    suspend fun getAppointment() : ResponseWrapper<AppointmentsModel>
}