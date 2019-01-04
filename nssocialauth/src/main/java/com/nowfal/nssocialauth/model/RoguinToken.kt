package com.nowfal.nssocialauth.model

import android.accounts.Account
import com.nowfal.nssocialauth.RoguinEndpoint
import kotlin.reflect.KClass

data class RoguinToken(

    val endpoint: KClass<out RoguinEndpoint>,

    val authenticatedToken: String,

    val userId: String,

    val account : Account
)