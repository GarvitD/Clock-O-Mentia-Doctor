package com.example.clock_o_mentiadoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.log.JitsiMeetLogger
import java.net.MalformedURLException
import java.net.URL

class MainActivity : JitsiMeetActivity() {
    override fun onConferenceTerminated(extraData: HashMap<String?, Any?>) {
//        super.onConferenceTerminated(extraData);
        JitsiMeetLogger.i("Conference terminated: $extraData", *arrayOfNulls(0))
        Log.e("workingFine", "conference terminated")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setRoom("testingClockomentia")
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            launch(this, options)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

    }
}