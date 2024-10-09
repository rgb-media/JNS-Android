package com.rgbmedia.jns.network.api

import com.rgbmedia.jns.network.model.LoginModel
import com.rgbmedia.jns.network.model.LoginParameters
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginApi {
    @Headers(
        "Accept: application/json",
        "app-secret: s2lekaqc8dmo73q7b8xs1fyddp2t2zn1"
    )

    @POST("login")
    fun login(@Body body: LoginParameters): Call<LoginModel>
}