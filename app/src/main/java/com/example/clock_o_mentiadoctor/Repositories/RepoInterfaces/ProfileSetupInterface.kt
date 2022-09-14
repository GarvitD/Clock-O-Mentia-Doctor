package com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces

import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupResponse

interface ProfileSetupInterface {
    suspend fun profileSetup(profileSetupBody: ProfileSetupBody): ResponseWrapper<ProfileSetupResponse>
}