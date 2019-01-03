package com.nowfal.nssocialauth

import com.nowfal.nssocialauth.helper.RoguinException
import com.nowfal.nssocialauth.model.RoguinProfile
import com.nowfal.nssocialauth.model.RoguinToken

interface RoguinEndpoint {

    /**
     * When {@code true}, there is an active Sing-In token present.
     */
    val isSignedIn: Boolean

    /**
     * Initiates a SignIn flow.
     *
     * @param response  The Unit to be invoked when the flow ends.
     *  Unit signature: (success: Boolean, token: RoguinToken?, error: RoguinException?)
     */
    fun requestSignIn(response: (success: Boolean, token: RoguinToken?, error: RoguinException?) -> Unit)

    /**
     * Initiates a SignOut flow.
     *
     * @param response  The Unit to be invoked when the flow ends.
     *  Unit signature: (success: Boolean)
     */
    fun requestSignOut(response: (success: Boolean) -> Unit)

    /**
     * Requests the authenticated user profile.
     *
     * @param resposne  The Unit to be invoked when the flow ends.
     *  Unit signature: (success: Boolean, profile: RoguinProfile?, error: RoguinException?)
     */
    fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit)

}