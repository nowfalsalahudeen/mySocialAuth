package com.nowfal.nssocialauth.model

import android.accounts.Account
import android.net.Uri

data class RoguinProfile(

    val email: String?,

    val name: String?,

    val photo: Uri?,
    val account: Account?
)