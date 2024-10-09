package com.rgbmedia.jns.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rgbmedia.jns.R
import com.rgbmedia.jns.network.api.LoginApi
import com.rgbmedia.jns.network.model.LoginModel
import com.rgbmedia.jns.network.model.LoginParameters
import com.rgbmedia.jns.ui.theme.jnsRed
import com.rgbmedia.jns.ui.theme.overlay
import com.rgbmedia.jns.utils.RgbConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun LoginView(showLoginDialog: MutableState<Boolean>) {
    val email = remember { mutableStateOf("gal@rgbmedia.org") }
    val password = remember { mutableStateOf("123") }

    Column {
        Spacer(
            Modifier
                .background(overlay)
                .weight(1f)
                .fillMaxWidth()
                .clickable(enabled = false) { }
        )

        HorizontalDivider(color = jnsRed, thickness = 10.dp)

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(512.dp)
        ) {
            Row {
                Spacer(
                    Modifier
                        .weight(1f)
                )

                TextButton(
                    onClick = {
                        showLoginDialog.value = false
                    },
                    modifier = Modifier
                        .padding(top = 13.dp, end = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.close),
                        contentDescription = null
                    )
                }
            }

            Text("Welcome to JNS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email Address*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(RgbConstants.LOGIN_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val service = retrofit.create(LoginApi::class.java)

                    val call = service.login(LoginParameters(email.value, password.value))

                    call.enqueue(object : Callback<LoginModel> {
                        override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                            if (response.isSuccessful) {
                                val loginModel = response.body()
                            } else {
                                response.errorBody()?.let { Log.d("AdiP", it.string()) }

                                val x= 0
                                // Handle error
                            }
                        }

                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            val x = 0
                        }
                    })
                }
            ) {
                Text("LOG IN")
            }
        }
    }
}