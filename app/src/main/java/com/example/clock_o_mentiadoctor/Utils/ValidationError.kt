package com.example.clock_o_mentiadoctor.Utils

import com.example.clock_o_mentiadoctor.R


enum class ValidationError(var code: Int) {
    EMPTY_NAME(R.string.empty_name),
    EMPTY_USERNAME(R.string.empty_username),
    EMPTY_DOB(R.string.empty_dob),
    EMPTY_MOBILE_NO(R.string.empty_mobile_no),
    INVALID_MOBILE_NO(R.string.invalid_mobile_no),
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PASSWORD(R.string.empty_password),
    INVALID_AGE(R.string.invalid_age),
    INVALID_PASSWORD(R.string.invalid_password),
    INVALID_EMAIL(R.string.invalid_email),
    EMPTY_OTP(R.string.empty_otp),
    INVALID_OTP(R.string.invalid_otp),
    EMPTY_GENDER(R.string.choose_gender),
    EMPTY_PROFILE_IMAGE(R.string.choose_profile_picture),
    EMPTY_ADDRESS(R.string.empty_address),
    EMPTY_CERTIFICATE(R.string.upload_certificate),
    NO_ERROR(R.string.no_error);
}