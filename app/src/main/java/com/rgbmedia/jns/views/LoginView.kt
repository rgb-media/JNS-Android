package com.rgbmedia.jns.views

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.rgbmedia.jns.R
import com.rgbmedia.jns.network.api.LoginApi
import com.rgbmedia.jns.network.model.LoginModel
import com.rgbmedia.jns.ui.theme.FreightSansProBoldFont
import com.rgbmedia.jns.ui.theme.FreightSansProBookFont
import com.rgbmedia.jns.ui.theme.UtopiaFont
import com.rgbmedia.jns.ui.theme.jnsBlack
import com.rgbmedia.jns.ui.theme.jnsBlue
import com.rgbmedia.jns.ui.theme.jnsRed
import com.rgbmedia.jns.ui.theme.overlay
import com.rgbmedia.jns.utils.RgbConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun LoginView(loginModel: MutableState<LoginModel?>, showLoginDialog: MutableState<Boolean>, showLoginPopup: MutableState<Boolean>) {
    val email = remember { mutableStateOf("adrian.picui@gmail.com") }
    val password = remember { mutableStateOf("123") }

    val context = LocalContext.current

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
                        .padding(top = 20.dp, end = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.close),
                        contentDescription = null
                    )
                }
            }

            HorizontalDivider(color = Color.Transparent, thickness = 16.dp)

            Text(
                text = "Welcome to JNS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                color = jnsBlack,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = UtopiaFont
            )

            HorizontalDivider(color = Color.Transparent, thickness = 30.dp)

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = {
                    Text(
                        text = "Email Address*",
                        color = jnsBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FreightSansProBookFont
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            )

            HorizontalDivider(color = Color.Transparent, thickness = 30.dp)

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = {
                    Text(
                        text = "Password*",
                        color = jnsBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FreightSansProBookFont
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            )

            HorizontalDivider(color = Color.Transparent, thickness = 10.dp)

            Text(
                text = "Forgot your password?",
                modifier = Modifier
                    .clickable(onClick = {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://crm.jns.org/log-in?referral=aHR0cHM6Ly93d3cuam5zLm9yZw==#ForgotPassword")
                        )
                        startActivity(context, browserIntent, null)
                    })
                    .padding(horizontal = 40.dp),
                color = Color(0xFF3399FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FreightSansProBookFont,
            )

            HorizontalDivider(color = Color.Transparent, thickness = 40.dp)

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.height(50.dp),
                    onClick = {
                        val interceptor = HttpLoggingInterceptor()
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                        val retrofit = Retrofit.Builder()
                            .baseUrl(RgbConstants.LOGIN_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        val service = retrofit.create(LoginApi::class.java)

                        val call = service.login(email.value, password.value)

                        call.enqueue(object : Callback<LoginModel> {
                            override fun onResponse(
                                call: Call<LoginModel>,
                                response: Response<LoginModel>
                            ) {
                                if (response.isSuccessful) {
                                    loginModel.value = response.body()
                                    showLoginPopup.value = true
                                } else {
                                    response.errorBody()?.let {
                                        val responseString = it.string()

                                        Log.d(RgbConstants.GENERAL_DEBUG_TAG, responseString)

                                        loginModel.value =
                                            Gson().fromJson(responseString, LoginModel::class.java)
                                        showLoginPopup.value = true
                                    }
                                }
                            }

                            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                                loginModel.value = LoginModel(
                                    t.localizedMessage,
                                    t.localizedMessage,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                                showLoginPopup.value = true
                            }
                        })
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = jnsBlack,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "LOG IN",
                        modifier = Modifier.padding(horizontal = 30.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FreightSansProBoldFont
                    )
                }
            }

            HorizontalDivider(color = Color.Transparent, thickness = 20.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    color = jnsBlack,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FreightSansProBoldFont
                )

                Text(
                    text = "Register now",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable(onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://crm.jns.org/sign-up")
                            )
                            startActivity(context, browserIntent, null)
                        }),
                    color = Color(0xFF3399FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FreightSansProBoldFont,
                )
            }

            HorizontalDivider(color = Color.Transparent, thickness = 40.dp)
        }
    }
}