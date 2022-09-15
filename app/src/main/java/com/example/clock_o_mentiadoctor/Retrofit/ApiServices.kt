package com.example.clock_o_mentiadoctor.Retrofit


import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.authentication.AuthResponse
import com.example.clock_o_mentiadoctor.models.authentication.LoginDetails
import com.example.clock_o_mentiadoctor.models.authentication.RegisterDetails
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupResponse
import retrofit2.http.*

interface ApiServices {

    @POST("/v1/docAuth/register")
    suspend fun signUp(
        @Body registerDetails: RegisterDetails
    ) : ResponseWrapper<AuthResponse>

    @POST("/v1/docAuth/login")
    suspend fun login(
        @Body loginDetails: LoginDetails
    ) : ResponseWrapper<AuthResponse>

    @POST("/v1/docInfos")
    suspend fun profileSetup(
        @Header("Authorization") accessToken: String,
        @Body profileSetupBody: ProfileSetupBody
    ) : ResponseWrapper<ProfileSetupResponse>

    @GET("/v1/appointments")
    suspend fun getAppointment(
        @Header("Authorization") accessToken: String,
        //@Query("doctorId") doctorId: String
    ):ResponseWrapper<AppointmentsModel>

    @PATCH("/v1/appointments")
    suspend fun updateAppointmentStatus(
        @Header("Authorization") accessToken: String,
        @Query("patientId")doctorId: String,
        //@Query("appointId")appointId:String,
        @Body appointmentsModel: Appointment
    ):ResponseWrapper<Appointment>
}