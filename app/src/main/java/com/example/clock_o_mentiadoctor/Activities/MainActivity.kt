package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.Utils.HelperClass
import com.example.clock_o_mentiadoctor.Utils.ProgressDialogClass
import com.example.clock_o_mentiadoctor.Utils.ResponseCode
import com.example.clock_o_mentiadoctor.Utils.SharedPreferenceManager
import com.example.clock_o_mentiadoctor.ViewModels.MainActivityViewModel
import com.example.clock_o_mentiadoctor.adapter.AppointmentAdapter
import com.example.clock_o_mentiadoctor.databinding.ActivityMainBinding
import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.AppointmentsModel
import com.example.clock_o_mentiadoctor.models.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , AppointmentAdapter.OnItemClick {
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var adapter: AppointmentAdapter
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    private val createLoginActivity = registerForActivityResult(LoginActivity) {}
    private val createViewAppointment = registerForActivityResult(ViewAppointment){}
    private val progressDialog = ProgressDialogClass(this)
    private var appointmentList = ArrayList<Appointment>()
    private val name by lazy { intent.getStringExtra(NAME) }
    private val isVerified by lazy { intent.getBooleanExtra(IS_VERIFIED,true) }
    private val doctorId by lazy {intent.getStringExtra(DOCTOR_ID)}
    private var id: String?=null
    private var firstAppointment: Appointment?=null
    private var bookedAppointment = ArrayList<Appointment>()
    private val sharedPreferenceManager = SharedPreferenceManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.recyclerView.adapter = adapter
        val token = "Bearer "+sharedPreferenceManager.getUserDetails(getString(R.string.auth_token))
        doctorId?.let { mainActivityViewModel.getAppointments(token, it) }
        initObservers()
        binding.drName.text = name
        binding.goToLogin.setOnClickListener{
            createLoginActivity.launch(null)
        }
        binding.accept.setOnClickListener {
            firstAppointment?.status = 1
            firstAppointment?.let { it1 -> id?.let { it2 ->
                doctorId?.let { it3 ->
                    mainActivityViewModel.updateAppointmentStatus(token, it3,
                        it2, it1)
                }
            } }
        }
        binding.decline.setOnClickListener {
            firstAppointment?.status = -1
            firstAppointment?.let { it1 -> id?.let { it2 ->
                doctorId?.let { it3 ->
                    mainActivityViewModel.updateAppointmentStatus(token, it3,
                        it2, it1)
                }
            } }
        }
        binding.info.setOnClickListener {
            if (appointmentList.size>0)
            viewAppointment()
        }
        binding.refreshBtn.setOnClickListener {
            updatePending()
        }
    }

    private fun viewAppointment() {
        createViewAppointment.launch(ViewAppointment.LaunchParams(appointmentList[0]))
    }

    private fun updatePending() {
        if (appointmentList.size>1){
            binding.name.text = appointmentList[0].name
            binding.dateTime.text = appointmentList[0].date
            id = appointmentList[0].patientId
            firstAppointment = appointmentList[0]
            appointmentList.removeAt(0)
        }
        else{
            binding.name.text = "XXXXXXX"
            binding.dateTime.text = "DD-MM-YY , HH:MM-HH:MM"
            HelperClass.toast(this,"List is Empty")
            binding.info.isClickable = false
        }
    }

    private fun initObservers() {
        mainActivityViewModel.appointmentResponse.observe(this) { response->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_200) {
                    appointmentList = response.data?.appointments?.filter { it.status ==0 } as ArrayList<Appointment>
                    id = appointmentList[0].patientId
                    firstAppointment = appointmentList[0]
                    binding.name.text = appointmentList[0].name
                    binding.dateTime.text = appointmentList[0].date

                    bookedAppointment = response.data?.appointments?.filter { it.status ==1 } as ArrayList<Appointment>
                    bookedAppointment.let {
                        adapter.setListOfAppointments(it,this) }
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                HelperClass.toast(this,response.message)
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }

        }

        mainActivityViewModel.appointmentStatusUpdateResponse.observe(this) { response->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_200) {
                    updatePending()
                    HelperClass.toast(this,"Updated appointment")
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                HelperClass.toast(this,response.message)
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }

        }
    }

    data class LaunchParams(
        val name: String? = null,
        val isVerified: Boolean? = false,
        val doctorId: String? = null
    )

    companion object: ActivityResultContract<LaunchParams, Boolean>() {
        var NAME = "name"
        var IS_VERIFIED = "isVerified"
        val DOCTOR_ID = "doctorId"
        override fun createIntent(context: Context, input: LaunchParams): Intent {
            val intent = Intent(context,MainActivity::class.java).apply {
                putExtra(NAME,input.name)
                putExtra(IS_VERIFIED,input.isVerified)
                putExtra(DOCTOR_ID,input.doctorId)
            }
            return intent
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

    override fun itemClick(appointmentModel: Appointment?) {
        createViewAppointment.launch(ViewAppointment.LaunchParams(appointmentModel))
    }
}