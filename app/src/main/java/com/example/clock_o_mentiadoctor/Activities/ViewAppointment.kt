package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.databinding.ActivityViewAppointmentBinding
import com.example.clock_o_mentiadoctor.models.Appointment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAppointment : AppCompatActivity() {


    private lateinit var binding: ActivityViewAppointmentBinding
    private val appointment by lazy { intent.getSerializableExtra(APPOINTMENT) }
    private var appointmentView: Appointment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_appointment)
        appointmentView = appointment as Appointment
        binding.appointment = appointmentView
        if (appointmentView?.status == 1) {
            binding.appointmentImg.setImageResource(R.drawable.calender_ticked)
            binding.appointmentStatus.text = "Booked"
        } else {
            binding.appointmentImg.setImageResource(R.drawable.waiting_icon)
            binding.appointmentStatus.text = "Awaiting"
        }

        binding.appointmentReport.setOnClickListener {
            val imagePopup = ImagePopup(this)

            imagePopup.windowHeight = 800
            imagePopup.windowWidth = 800
            imagePopup.backgroundColor = ContextCompat.getColor(this, R.color.black)
            imagePopup.isFullScreen = true
            imagePopup.isHideCloseIcon = true
            imagePopup.isImageOnClickClose = true
            imagePopup.initiatePopupWithGlide(appointmentView?.let { it.reportUrl })

            imagePopup.viewPopup()
        }

    }

    data class LaunchParams(
        val appointment: Appointment? = null
    )


    companion object: ActivityResultContract<LaunchParams, Boolean>() {
        var APPOINTMENT = "appointment"
        override fun createIntent(context: Context, input: LaunchParams): Intent {
            val intent = Intent(context,ViewAppointment::class.java).apply {
                putExtra(APPOINTMENT,input.appointment)
            }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }
}