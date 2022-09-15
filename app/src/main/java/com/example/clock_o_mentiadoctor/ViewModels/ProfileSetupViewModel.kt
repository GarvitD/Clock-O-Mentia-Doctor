package com.example.clock_o_mentiadoctor.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.ProfileSetupInterface
import com.example.clock_o_mentiadoctor.Utils.ValidationError
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(var profileSetupInterface: ProfileSetupInterface): ViewModel() {

    var profileSetupResponse = MutableLiveData<ResponseWrapper<ProfileSetupResponse>>()

    fun checkProfileSetUp(profileSetupBody: ProfileSetupBody) : ValidationError {
        if (profileSetupBody.age.isNullOrEmpty()){
            return ValidationError.INVALID_AGE
        }
        else if (profileSetupBody.gender.isNullOrEmpty()){
            return ValidationError.EMPTY_GENDER
        }
        else if (profileSetupBody.phone.isNullOrEmpty()){
            return ValidationError.EMPTY_MOBILE_NO
        }
        else if(profileSetupBody.phone.toString().length!=10){
            return ValidationError.INVALID_MOBILE_NO
        }
        else if(profileSetupBody.address.isNullOrEmpty()){
            return ValidationError.EMPTY_ADDRESS
        }
        else if (profileSetupBody.profileImage.isNullOrEmpty()){
            return ValidationError.EMPTY_PROFILE_IMAGE
        }
        else if (profileSetupBody.certificate.isNullOrEmpty()){
            return ValidationError.EMPTY_CERTIFICATE
        }
        return ValidationError.NO_ERROR
    }

    fun profileSetup(accessToken:String,profileSetupBody: ProfileSetupBody){
        viewModelScope.launch {
            profileSetupResponse.postValue(ResponseWrapper.loading())
            try{
                val response = profileSetupInterface.profileSetup(accessToken,profileSetupBody)
                profileSetupResponse.postValue(ResponseWrapper.success(response))
            }
            catch (e: Exception){
                profileSetupResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }
}