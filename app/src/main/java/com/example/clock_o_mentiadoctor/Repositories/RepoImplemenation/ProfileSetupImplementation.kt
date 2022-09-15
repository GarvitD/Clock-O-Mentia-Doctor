package com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation

import android.util.Log
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.ProfileSetupInterface
import com.example.clock_o_mentiadoctor.Retrofit.ApiServices
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupResponse
import javax.inject.Inject

class ProfileSetupImplementation @Inject constructor(var apiServices: ApiServices): ProfileSetupInterface {

    override suspend fun profileSetup(accessToken:String,profileSetupBody: ProfileSetupBody): ResponseWrapper<ProfileSetupResponse> {
        return apiServices.profileSetup(accessToken,profileSetupBody,)
    }

}