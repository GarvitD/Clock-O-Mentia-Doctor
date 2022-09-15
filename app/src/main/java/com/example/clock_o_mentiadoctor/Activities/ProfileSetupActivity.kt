package com.example.clock_o_mentiadoctor.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiadoctor.R
import com.example.clock_o_mentiadoctor.Utils.HelperClass
import com.example.clock_o_mentiadoctor.Utils.ProgressDialogClass
import com.example.clock_o_mentiadoctor.Utils.ResponseCode
import com.example.clock_o_mentiadoctor.Utils.ValidationError
import com.example.clock_o_mentiadoctor.ViewModels.ProfileSetupViewModel
import com.example.clock_o_mentiadoctor.databinding.ActivityProfileSetupBinding
import com.example.clock_o_mentiadoctor.models.NetworkState
import com.example.clock_o_mentiadoctor.models.profile.ProfileSetupBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileSetupBinding
    private val profileViewModel by viewModels<ProfileSetupViewModel>()
    private val createMainActivity = registerForActivityResult(MainActivity) {}
    private val name by lazy { intent.getStringExtra(NAME) }
    private val email by lazy { intent.getStringExtra(EMAIL) }
    private val id by lazy { intent.getStringExtra(ID) }
    private val progressDialog = ProgressDialogClass(this)
    private val genders = arrayOf("Male", "Female", "Others")
    private var clicked = 0

    private var certificates: StorageReference? = null
    private var profileImages: StorageReference? = null
    private var firestoreRef: CollectionReference? = null
    private val GET_IMG_REQID:Int = 16
    private var certificate_link:String? = "test.com"
    private var profile_link:String? = "test.com"
    private var profileImageUri: Uri? = null
    private var certiImageUri: Uri? = null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile_setup)

        binding.name = name
        binding.email = email
        certificates = FirebaseStorage.getInstance().getReference("doctor_certi")
        profileImages = FirebaseStorage.getInstance().getReference("doctor_profile_images")
        firestoreRef = FirebaseFirestore.getInstance().collection("doctors_info")
        initObservers()

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, genders)
        binding.genderOptions.setAdapter(arrayAdapter)

        binding.certiImage.setOnClickListener {
            clicked = 1
            bringImage()
        }

        binding.profileImage.setOnClickListener {
            clicked = 2
            bringImage()
        }

        binding.uploadImage.setOnClickListener { uploadImage() }

        binding.doctorSaveDetails.setOnClickListener {
            setDoctorProfile()
        }

    }

    private fun initObservers() {
        profileViewModel.profileSetupResponse.observe(this) { response->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_201) {
                    createMainActivity.launch(MainActivity.LaunchParams(response.data?.name,response.data?.isVerified))
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

    private fun setDoctorProfile() {
        val age = binding.doctorAge.text.toString()
        val mobileNo = binding.doctorPhone.text.toString()
        val address = binding.addressInput.text.toString()
        val gender = binding.genderOptions.text.toString()
        val profileSetupDetails = ProfileSetupBody(name = name, email = email, doctorId = id, age = age, gender = gender, address = address, phone = mobileNo, profileImage = profile_link, certificate = certificate_link)
        when(val error = profileViewModel.checkProfileSetUp(profileSetupDetails)){
            ValidationError.INVALID_AGE -> binding.doctorAge.error = getString(error.code)
            ValidationError.EMPTY_GENDER -> binding.genderOptions.error = getString(error.code)
            ValidationError.EMPTY_PROFILE_IMAGE -> HelperClass.toast(this,getString(error.code))
            ValidationError.EMPTY_MOBILE_NO -> binding.phone.error = getString(error.code)
            ValidationError.INVALID_MOBILE_NO -> binding.phone.error = getString(error.code)
            ValidationError.EMPTY_ADDRESS -> binding.address.error = getString(error.code)
            ValidationError.EMPTY_CERTIFICATE -> HelperClass.toast(this,getString(error.code))


            else -> profileViewModel.profileSetup(profileSetupDetails)
        }

    }


    private fun getFileExtension(imageUri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri))
    }

    private fun uploadImage() {
        if (certiImageUri != null && profileImageUri != null) {
            val certi = certificates!!.child(System.currentTimeMillis().toString() + "." + getFileExtension(certiImageUri!!))
            val profile_img = profileImages!!.child(System.currentTimeMillis().toString() + "." + getFileExtension(profileImageUri!!))
            certi.putFile(certiImageUri!!).addOnSuccessListener {
                certi.downloadUrl.addOnSuccessListener { uri -> certificate_link = uri.toString() }
                profile_img.putFile(profileImageUri!!)
                        .addOnSuccessListener { profile_img.downloadUrl.addOnSuccessListener { uri ->
                                profile_link = uri.toString()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener {
                            Toast.makeText(this, "Please wait for upload to get over", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                    Toast.makeText(this, "Please wait for upload to get over", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Choose an Image!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bringImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,GET_IMG_REQID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_IMG_REQID && resultCode == RESULT_OK && data != null && data.data != null) {
            if (clicked == 1) {
                certiImageUri = data.data
                binding.certiImage.setImageURI(certiImageUri)
            } else if (clicked == 2) {
                profileImageUri = data.data
                binding.profileImage.setImageURI(profileImageUri)
            }
        }
    }

    data class LaunchParams(
        val name: String? = null,
        val email: String? = null,
        val id: String? = null
    )

    companion object : ActivityResultContract<LaunchParams, Boolean>() {
        var NAME = "name"
        var EMAIL = "email"
        var ID ="id"
        override fun createIntent(context: Context, input: LaunchParams): Intent {
            val intent = Intent(context, ProfileSetupActivity::class.java).apply {
                putExtra(NAME, input.name)
                putExtra(EMAIL, input.email)
                putExtra(ID,input.id)
            }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

}