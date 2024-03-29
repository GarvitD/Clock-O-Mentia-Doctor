package com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation

import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AppointmentInterface
import com.example.clock_o_mentiadoctor.Retrofit.ApiServices
import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import javax.inject.Inject

class AppointmentRepoImplementation @Inject constructor(var apiServices: ApiServices): AppointmentInterface {
    override suspend fun getAppointment(accessToken:String,doctorId:String): ResponseWrapper<AppointmentsModel> {
        return apiServices.getAppointment(accessToken)
    }

    override suspend fun updateAppointmentStatus(accessToken:String,doctorId:String,id: String, appointment: Appointment): ResponseWrapper<Appointment> {
        return apiServices.updateAppointmentStatus(accessToken,id,appointment)
    }
}