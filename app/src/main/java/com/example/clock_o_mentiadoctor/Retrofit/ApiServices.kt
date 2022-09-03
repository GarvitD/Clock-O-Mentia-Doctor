package com.example.clock_o_mentiadoctor.Retrofit


import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.authentication.AuthResponse
import com.example.clock_o_mentiadoctor.models.authentication.LoginDetails
import com.example.clock_o_mentiadoctor.models.authentication.RegisterDetails
import retrofit2.http.*

interface ApiServices {

    @POST("/v1/auth/register")
    suspend fun signUp(
        @Body registerDetails: RegisterDetails
    ) : ResponseWrapper<AuthResponse>

    @POST("/v1/auth/login")
    suspend fun login(
        @Body loginDetails: LoginDetails
    ) : ResponseWrapper<AuthResponse>
}