package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.Utils.*
import com.example.clock_o_mentiadoctor.ViewModels.AuthViewModel
import com.example.clock_o_mentiadoctor.databinding.ActivityRegisterBinding
import com.example.clock_o_mentiadoctor.models.NetworkState
import com.example.clock_o_mentiadoctor.models.authentication.RegisterDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel by viewModels<AuthViewModel>()
    private lateinit var  activityRegisterBinding: ActivityRegisterBinding
    private val createLoginActivity = registerForActivityResult(LoginActivity) {}
    private val createProfileSetupActivity = registerForActivityResult(ProfileSetupActivity) {}
    private val progressDialog = ProgressDialogClass(this)
    private val sharedPreferenceManager = SharedPreferenceManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initObservers()

        activityRegisterBinding.signUpBtn.setOnClickListener {
            registerUser()
        }

        activityRegisterBinding.directToLogin.setOnClickListener {
           createLoginActivity.launch(null)
        }
    }

    private fun registerUser() {
        val name = activityRegisterBinding.fullName.text.toString()
        val password = activityRegisterBinding.password.text.toString()
        val email = activityRegisterBinding.emailAddress.text.toString().trim()
        val confPassword = activityRegisterBinding.confirmPassword.text.toString()

        if(password != confPassword) {
            activityRegisterBinding.inputConfirmPassword.error = getString(R.string.password_not_match)
            return
        }
        val registerDetails = RegisterDetails(name=name,email=email,password=password)

        when (val error = registerViewModel.checkSignUpData(registerDetails)) {
            ValidationError.EMPTY_EMAIL -> activityRegisterBinding.inputEmailAddress.error = getString(error.code)
            ValidationError.EMPTY_PASSWORD -> activityRegisterBinding.inputPassword.error = getString(error.code)
            ValidationError.EMPTY_NAME -> activityRegisterBinding.inputFullName.error = getString(error.code)
            ValidationError.INVALID_PASSWORD -> activityRegisterBinding.inputPassword.error = getString(error.code)
            ValidationError.INVALID_EMAIL -> activityRegisterBinding.inputEmailAddress.error = getString(error.code)
            else -> registerViewModel.register(registerDetails)
        }
    }

    private fun initObservers() {
        registerViewModel.registerResponse.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_201) {
                    progressDialog.dismiss()
                    val id = response.data?.user?.id
                    sharedPreferenceManager
                        .saveUserDetails(getString(R.string.email_address),activityRegisterBinding.emailAddress.text.toString())
                    createProfileSetupActivity.launch(ProfileSetupActivity.LaunchParams( activityRegisterBinding.fullName.text.toString(),activityRegisterBinding.emailAddress.text.toString().trim(),id))
                    finish()
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                if(response.code == ResponseCode.CODE_411) {
                    activityRegisterBinding.inputEmailAddress.error = response.message
                } else {
                    HelperClass.toast(this,response.message)
                }
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }
        }
    }

    companion object: ActivityResultContract<String?, Boolean>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(context,RegisterActivity::class.java).apply {

            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }
}