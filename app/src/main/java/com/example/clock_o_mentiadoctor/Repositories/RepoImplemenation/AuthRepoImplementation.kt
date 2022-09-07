package com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation


import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AuthRepoInterface
import com.example.clock_o_mentiadoctor.Retrofit.ApiServices
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.authentication.AuthResponse
import com.example.clock_o_mentiadoctor.models.authentication.LoginDetails
import com.example.clock_o_mentiadoctor.models.authentication.RegisterDetails
import javax.inject.Inject

class AuthRepoImplementation @Inject constructor(var apiServices: ApiServices) : AuthRepoInterface {
    override suspend fun signUp(registerDetails: RegisterDetails): ResponseWrapper<AuthResponse> {
        return apiServices.signUp(registerDetails)
    }

    override suspend fun login(loginDetails: LoginDetails): ResponseWrapper<AuthResponse> {
        return apiServices.login(loginDetails)
    }
}