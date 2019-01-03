package com.izikode.izilib.roguin.endpoint

import android.content.Context
import android.net.Uri
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.nowfal.nssocialauth.helper.NsSocialActivity
import com.nowfal.nssocialauth.RoguinEndpoint
import com.nowfal.nssocialauth.helper.RoguinException
import com.nowfal.nssocialauth.model.RoguinProfile
import com.nowfal.nssocialauth.model.RoguinToken
import android.os.Bundle
import org.json.JSONException
import org.json.JSONObject
import com.facebook.GraphRequest
import com.nowfal.nssocialauth.helper.UserNotSignedInException


class FacebookEndpoint(

        private val nsSocialActivity: NsSocialActivity

) : RoguinEndpoint {

    private val facebookLoginButton = LoginButton(nsSocialActivity).apply {
        setReadPermissions(arrayListOf("public_profile", "email"))
    }

    override val isSignedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

    override fun requestSignIn(response: (success: Boolean, token: RoguinToken?, error: RoguinException?) -> Unit) {
        CallbackManager.Factory.create().let { callbackManager ->
            facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {
                    facebookLoginButton.removeCallbacks {}
                    nsSocialActivity.unregisterCallbackManager(callbackManager)

                    if (result != null) {
                        response.invoke(true, result.toToken(), null)
                    } else {
                        response.invoke(false, null, null)
                    }
                }

                override fun onCancel() {
                    facebookLoginButton.removeCallbacks {}
                    nsSocialActivity.unregisterCallbackManager(callbackManager)

                    response.invoke(false, null, null)
                }

                override fun onError(error: FacebookException?) {
                    facebookLoginButton.removeCallbacks {}
                    nsSocialActivity.unregisterCallbackManager(callbackManager)

                    response.invoke(false, null, RoguinException(error))
                }

            })

            nsSocialActivity.registerCallbackManager(callbackManager)
            facebookLoginButton.performClick()
        }
    }

    private fun LoginResult.toToken() = RoguinToken(
        endpoint = this@FacebookEndpoint::class,
        authenticatedToken = this.accessToken.token,
        userId = this.accessToken.userId
    )

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {

            if (it.error == null) {
                LoginManager.getInstance().logOut()
                response.invoke(true)
            } else {
                response.invoke(false)
            }

        }).executeAsync()
    }

    override fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit) {
        val token = AccessToken.getCurrentAccessToken()

        if (token == null || token.isExpired) {
            response.invoke(false, null, UserNotSignedInException())
        } else {
            GraphRequest.newMeRequest(token) { jsonObject, graphResponse ->
                try {
                    response.invoke(true, jsonObject.toProfile(), null)
                } catch (e: JSONException) {
                    response.invoke(false, null, RoguinException(e))
                }
            }.apply {
                parameters = Bundle().apply {
                    putString("fields", "email, name, picture")
                }
            }.executeAsync()
        }
    }

    private fun JSONObject.toProfile() = RoguinProfile(
        email = this.getString("email"),
        name = this.getString("name"),
        photo = this.getJSONObject("picture")?.getJSONObject("data")?.getString("url")?.let { Uri.parse(it) }
    )

    companion object {

        @JvmStatic
        fun initialize(applicationContext: Context) {
            /* Reserved for upcoming functionality */
        }

    }

}