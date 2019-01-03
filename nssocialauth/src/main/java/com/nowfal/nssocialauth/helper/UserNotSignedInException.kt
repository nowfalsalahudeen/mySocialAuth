package com.nowfal.nssocialauth.helper

import com.nowfal.nssocialauth.helper.RoguinException

class UserNotSignedInException : RoguinException(internalMessage = "User requested is not signed in.")