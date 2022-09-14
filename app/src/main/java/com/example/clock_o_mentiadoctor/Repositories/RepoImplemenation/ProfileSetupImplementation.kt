package com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation

import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.ProfileSetupInterface
import com.example.clock_o_mentiadoctor.Retrofit.ApiServices
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupResponse
import javax.inject.Inject

class ProfileSetupImplementation @Inject constructor(var apiServices: ApiServices): ProfileSetupInterface {

    override suspend fun profileSetup(profileSetupBody: ProfileSetupBody): ResponseWrapper<ProfileSetupResponse> {
        return apiServices.profileSetup(profileSetupBody)
    }

}