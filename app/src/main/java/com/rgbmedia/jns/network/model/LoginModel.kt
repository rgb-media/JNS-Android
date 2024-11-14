package com.rgbmedia.jns.network.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    var error: String?,
    var message: String?,
    var id: Int?,
    var status: String?,
    var hasSubscription: Boolean?,
    var hasComments: Boolean?,
    var firstName: String?,
    var lastName: String?,
    @SerializedName("CRMSESSION") var crmSession: String?
)
