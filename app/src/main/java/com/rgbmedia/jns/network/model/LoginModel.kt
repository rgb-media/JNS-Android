package com.rgbmedia.jns.network.model

data class LoginModel(
    var error: String?,
    var message: String?,
    var id: Int?,
    var status: String?,
    var hasSubscription: Boolean?,
    var hasComments: Boolean?,
    var firstName: String?,
    var lastName: String?
)

data class LoginParameters(
    var email: String,
    var password: String
)
