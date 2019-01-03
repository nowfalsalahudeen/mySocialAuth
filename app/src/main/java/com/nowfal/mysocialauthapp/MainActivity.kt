package com.nowfal.mysocialauthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.nowfal.nssocialauth.endpoint.GoogleEndpoint
import com.nowfal.nssocialauth.helper.NsSocialActivity


class MainActivity : NsSocialActivity() {
    private val googleEndpoint by lazy { GoogleEndpoint(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (googleEndpoint.isSignedIn) {
            googleEndpoint.requestSignOut { success ->
                if (success) {
                    println("Google is DISCONNECTED")
                }
            }
        } else {
            googleEndpoint.requestSignIn { success, token, error ->
                if (success) {
                    Log.d("Google TOKEN", token.toString())
                    println("Google is CONNECTED")
                }else{
                    error?.printStackTrace()
                }
            }
        }

    }
}
