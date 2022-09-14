package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.Utils.HelperClass
import com.example.clock_o_mentiadoctor.Utils.ProgressDialogClass
import com.example.clock_o_mentiadoctor.Utils.ResponseCode
import com.example.clock_o_mentiadoctor.ViewModels.MainActivityViewModel
import com.example.clock_o_mentiadoctor.adapter.AppointmentAdapter
import com.example.clock_o_mentiadoctor.databinding.ActivityMainBinding
import com.example.clock_o_mentiadoctor.models.Appointment
import com.example.clock_o_mentiadoctor.models.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , AppointmentAdapter.OnItemClick {
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var adapter: AppointmentAdapter
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    private val createLoginActivity = registerForActivityResult(LoginActivity) {}
    private val progressDialog = ProgressDialogClass(this)
    private var appointmentList = ArrayList<Appointment>()
    private val name by lazy { intent.getStringExtra(NAME) }
    private val isVerified by lazy { intent.getBooleanExtra(IS_VERIFIED,false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.recyclerView.adapter = adapter
        mainActivityViewModel.getAppointments()
        initObservers()
        binding.drName.text = name
        if (isVerified){
            binding.doctorVerified.isVisible = true
        }
        else{
            binding.docNotVerified.isVisible = true
        }
        binding.goToLogin.setOnClickListener{
            createLoginActivity.launch(null)
        }
        binding.accept.setOnClickListener {
            updatePending()
        }
        binding.decline.setOnClickListener {
            updatePending()
        }
        binding.info.setOnClickListener {
            viewAppointment()
        }
        binding.refreshBtn.setOnClickListener {
            updatePending()
        }
    }

    private fun viewAppointment() {

    }

    private fun updatePending() {
        if (appointmentList.size!=0){
            binding.name.text = appointmentList[0].name
            binding.dateTime.text = appointmentList[0].date
            appointmentList.removeAt(0)
            adapter.setListOfAppointments(appointmentList,this)
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

                if (response.code == ResponseCode.CODE_201) {
                    binding.name.text = response.data?.appointments?.get(0)?.name
                    binding.dateTime.text = response.data?.appointments?.get(0)?.date
                    response.data?.appointments?.let {
                        it.removeAt(0)
                        appointmentList = it
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
    }

    data class LaunchParams(
        val name: String? = null,
        val isVerified: Boolean? = false
    )

    companion object: ActivityResultContract<LaunchParams, Boolean>() {
        var NAME = "name"
        var IS_VERIFIED = "isVerified"
        override fun createIntent(context: Context, input: LaunchParams): Intent {
            val intent = Intent(context,MainActivity::class.java).apply {
                putExtra(NAME,input.name)
                putExtra(IS_VERIFIED,input.isVerified)
            }
            return intent
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

    override fun itemClick(appointmentModel: Appointment?) {

    }
}