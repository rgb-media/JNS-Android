package com.rgbmedia.jns.utils

object RgbConstants {
    const val GENERAL_DEBUG_TAG = "RGBDebug"

    const val SERVER        = "www"
    const val DOMAIN_NAME   = "jns.org"
    const val BASE_URL      = "https://" + SERVER + "." + DOMAIN_NAME + "/"
    var LOGIN_URL           = "https://" + (if (SERVER == "dev") "dev" else "") + "crm.jns.org/api/"

    const val CRMUSER_COOKIE = "crmUser" // "CrmUser"
    const val USERID_COOKIE = "userId"
    const val CRMSESSION_COOKIE = "CRMSESSION"
}
