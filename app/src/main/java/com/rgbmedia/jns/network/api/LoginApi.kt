package com.rgbmedia.jns.network.api

import com.rgbmedia.jns.network.model.LoginModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @Headers(
        "app-secret: s2lekaqc8dmo73q7b8xs1fyddp2t2zn1"
    )
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginModel>
}