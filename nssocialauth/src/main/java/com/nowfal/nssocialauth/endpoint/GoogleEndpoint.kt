package com.nowfal.nssocialauth.endpoint

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nowfal.nssocialauth.helper.NsSocialActivity
import com.nowfal.nssocialauth.RoguinEndpoint
import com.nowfal.nssocialauth.helper.RoguinException
import com.nowfal.nssocialauth.helper.UserNotSignedInException
import com.nowfal.nssocialauth.model.RoguinProfile
import com.nowfal.nssocialauth.model.RoguinToken
import android.content.pm.PackageManager
import android.util.Log


class GoogleEndpoint(

        private val nsSocialActivity: NsSocialActivity

) : RoguinEndpoint {

    private var myApiKey: String? = null
    private val googleClient by lazy {

        try {
            val ai = nsSocialActivity.packageManager.getApplicationInfo(nsSocialActivity.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            myApiKey = bundle.getString("google_server_client_id")
        } catch (e: Exception) {
            Log.e(
                "nsLibrary",
                "Dear developer. Don't forget to configure <meta-data android:name=\"google_server_client_id\" android:value=\"testValue\"/> in your AndroidManifest.xml file."
            )
        }

        println("key $myApiKey")
        val options = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(myApiKey)
            .requestProfile()
            .requestEmail()
            .build()

        GoogleSignIn.getClient(nsSocialActivity, options)
    }

    override val isSignedIn: Boolean
        get() = GoogleSignIn.getLastSignedInAccount(nsSocialActivity) != null

    override fun requestSignIn(response: (success: Boolean, token: RoguinToken?, error: RoguinException?) -> Unit) {
        nsSocialActivity.requestResult(googleClient.signInIntent) { success, result ->
            if (!success) {
//                println(result?.extras)
//                val bundle = result?.extras
//                if (bundle != null) {
//                    for (key in bundle!!.keySet()) {
//                        val value = bundle!!.get(key)
//                        Log.d(
//                            TAG, String.format(
//                                "%s %s (%s)", key,
//                                value!!.toString(), value!!.javaClass.name
//                            )
//                        )
//                    }
//                }

                response.invoke(false, null, RoguinException(null, result))
            } else {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result)

                try {
                    val taskResult = task.getResult(ApiException::class.java)

                    response.invoke(true, taskResult?.toToken(), null)
                } catch (googleApiException: ApiException) {
                    response.invoke(false, null, RoguinException(googleApiException, result))
                }
            }
        }
    }

    private fun GoogleSignInAccount.toToken() = RoguinToken(
        endpoint = this@GoogleEndpoint::class,
        authenticatedToken = this.idToken ?: "",
        userId = this.id ?: "",
        account = this.account!!
    )

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        googleClient.signOut().addOnCompleteListener {
            response.invoke(it.isSuccessful)
        }
    }

    override fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit) {
        val lastAccount = GoogleSignIn.getLastSignedInAccount(nsSocialActivity)

        if (lastAccount != null) {
            response.invoke(true, lastAccount.toProfile(), null)
        } else {
            response.invoke(false, null, UserNotSignedInException())
        }
    }

    private fun GoogleSignInAccount.toProfile() = RoguinProfile(
        email = this.email,
        name = this.displayName,
        photo = this.photoUrl
    )

    companion object {

        @JvmStatic
        fun initialize(applicationContext: Context) {
            /* Reserved for upcoming functionality */
        }

    }

}