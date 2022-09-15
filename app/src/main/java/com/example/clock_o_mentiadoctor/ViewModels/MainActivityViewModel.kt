package com.example.clock_o_mentiadoctor.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AppointmentInterface
import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(var appointmentInterface: AppointmentInterface): ViewModel() {

    var appointmentResponse = MutableLiveData<ResponseWrapper<AppointmentsModel>>()
    var appointmentStatusUpdateResponse = MutableLiveData<ResponseWrapper<Appointment>>()

    fun getAppointments(accessToken:String,doctorId:String){
        viewModelScope.launch {
            appointmentResponse.postValue(ResponseWrapper.loading())
            try{
                val response = appointmentInterface.getAppointment(accessToken,doctorId)
                appointmentResponse.postValue(ResponseWrapper.success(response))
            }
            catch (e: Exception){
                appointmentResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }

    fun updateAppointmentStatus(accessToken:String,doctorId:String,id: String,appointment:Appointment){
        viewModelScope.launch {
            appointmentStatusUpdateResponse.postValue(ResponseWrapper.loading())
            try{
                val response = appointmentInterface.updateAppointmentStatus(accessToken,doctorId,id,appointment)
                appointmentStatusUpdateResponse.postValue(ResponseWrapper.success(response))
            }
            catch (e: Exception){
                appointmentStatusUpdateResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }
}