package com.example.clock_o_mentiadoctor.models

enum class NetworkState(var code : Int) {
    LOADING(0),
    SUCCESS(1),
    ERROR(-1);
}
