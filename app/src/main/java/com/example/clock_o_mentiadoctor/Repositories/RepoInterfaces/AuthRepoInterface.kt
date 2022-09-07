package com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces

import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.authentication.AuthResponse
import com.example.clock_o_mentiadoctor.models.authentication.LoginDetails
import com.example.clock_o_mentiadoctor.models.authentication.RegisterDetails

interface AuthRepoInterface {
    suspend fun signUp(registerDetails: RegisterDetails) : ResponseWrapper<AuthResponse>
    suspend fun login(loginDetails: LoginDetails) : ResponseWrapper<AuthResponse>
}