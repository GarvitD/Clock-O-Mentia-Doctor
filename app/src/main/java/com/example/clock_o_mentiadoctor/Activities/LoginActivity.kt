package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.Utils.*
import com.example.clock_o_mentiadoctor.ViewModels.AuthViewModel
import com.example.clock_o_mentiadoctor.databinding.ActivityLoginBinding
import com.example.clock_o_mentiadoctor.models.NetworkState
import com.example.clock_o_mentiadoctor.models.authentication.LoginDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<AuthViewModel>()
    private lateinit var activityLoginBinding : ActivityLoginBinding
    private val createMainActivity = registerForActivityResult(MainActivity) {}
    private val createRegisterActivity = registerForActivityResult(RegisterActivity) {}
    private val progressDialog = ProgressDialogClass(this)
    private val sharedPreferenceManager = SharedPreferenceManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initObservers()

        activityLoginBinding.directToRegister.setOnClickListener {
            createRegisterActivity.launch(null)
        }

        activityLoginBinding.loginBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val password = activityLoginBinding.password.text.toString()
        val email = activityLoginBinding.emailAddress.text.toString()

        val loginDetails = LoginDetails(email, password)

        when (val error = loginViewModel.checkLoginData(loginDetails)) {
            ValidationError.EMPTY_EMAIL -> activityLoginBinding.inputEmailAddress.error = getString(error.code)
            ValidationError.EMPTY_PASSWORD -> activityLoginBinding.inputPassword.error = getString(error.code)
            else -> loginViewModel.login(loginDetails)
        }
    }

    private fun initObservers() {
        loginViewModel.loginResponse.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_200) {

                    progressDialog.dismiss()
                    response.data?.user?.let {
                        sharedPreferenceManager
                            .saveUserDetails(getString(R.string.user_details),it.toString())
                        response.data?.tokens?.access?.token?.let {
                            sharedPreferenceManager.
                            saveUserDetails(getString(R.string.auth_token), it)
                        }
                    }
                    createMainActivity.launch(MainActivity.LaunchParams(response.data?.user?.name,false,response.data?.user?.id))
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

    companion object: ActivityResultContract<String?, Boolean>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(context,LoginActivity::class.java).apply {

            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }
}