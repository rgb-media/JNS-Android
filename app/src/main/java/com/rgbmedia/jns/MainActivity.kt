package com.rgbmedia.jns

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.rgbmedia.jns.network.model.LoginModel
import com.rgbmedia.jns.ui.theme.jnsBlue
import com.rgbmedia.jns.ui.theme.jnsRed
import com.rgbmedia.jns.utils.RgbConstants
import com.rgbmedia.jns.views.LoginView
import com.rgbmedia.jns.webview.RgbWebView

val headerHeight = 60.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(true)

        setContent {
            val url = remember { mutableStateOf(RgbConstants.BASE_URL) }
            val showJavascriptDialog = remember { mutableStateOf(false) }
            val showLoginDialog = remember { mutableStateOf(false) }
            val showLoginPopup = remember { mutableStateOf(false) }
            val loginModel = remember { mutableStateOf<LoginModel?>(null) }

            if (showLoginPopup.value) {
                var message = "Logged in as " + loginModel.value?.firstName + " " + loginModel.value?.lastName
                if (loginModel.value?.error != null) {
                    message = loginModel.value?.message!!
                } else {
                    val useridString = RgbConstants.USERID_COOKIE + "=" + loginModel.value?.id + "; expires=Sat, 01-Jan-2030 00:00:00 GMT; path=/"
                    CookieManager.getInstance().setCookie(".jns.org", useridString)

                    val userJson = Gson().toJson(loginModel.value)
                    val crmuserString = RgbConstants.CRMUSER_COOKIE + "=" + userJson + "; expires=Sat, 01-Jan-2030 00:00:00 GMT; path=/"
                    CookieManager.getInstance().setCookie(".jns.org", crmuserString)

                    val crmSession = RgbConstants.CRMSESSION_COOKIE + "=" + loginModel.value?.crmSession + "; expires=Sat, 01-Jan-2030 00:00:00 GMT; path=/"
                    CookieManager.getInstance().setCookie(".jns.org", crmSession) {
                        url.value = "/"
                        url.value = RgbConstants.BASE_URL
                    }
                }

                AlertDialog(
                    onDismissRequest = { showLoginPopup.value = false },
                    title = { Text("Login result") },
                    text = {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            Text(text = message)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showLoginPopup.value = false
                            showLoginDialog.value = false
                        }) {
                            Text("OK".uppercase())
                        }
                    }
                )
            }

            if (showJavascriptDialog.value) {
                AlertDialog(
                    onDismissRequest = { showJavascriptDialog.value = false },
                    title = { Text("Javascript function called") },
                    text = {
                        Text(text = "openNativeLoginDialog")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showJavascriptDialog.value = false
                        }) {
                            Text("OK".uppercase())
                        }
                    }
                )
            }

            Box {
                Column {
                    Header(showLoginDialog)
                    RgbWebView(url, showJavascriptDialog)
                }

                Image(
                    painter = painterResource(R.drawable.jns_logo_in_frame),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

                if (showLoginDialog.value) {
                    LoginView(loginModel, showLoginDialog, showLoginPopup)
                }
            }
        }
    }
}

@Composable
fun Header(showLoginDialog: MutableState<Boolean>) {
    val showAllCookiesDialog = remember { mutableStateOf(false) }

    if (showAllCookiesDialog.value) {
        val cookies = CookieManager.getInstance().getCookie(RgbConstants.BASE_URL).replace(";", "\n\n")

        AlertDialog(
            onDismissRequest = { showAllCookiesDialog.value = false },
            title = { Text("List of all cookies") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(text = cookies)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showAllCookiesDialog.value = false
                }) {
                    Text("OK".uppercase())
                }
            }
        )
    }

    Column {
        HorizontalDivider(color = jnsRed, thickness = 4.dp)
        HorizontalDivider(color = jnsBlue, thickness = 2.dp)

        Row(
            modifier = Modifier
                .height(headerHeight)
        ) {
            Spacer(
                Modifier
                    .weight(1f)
                    .fillMaxHeight())

            VerticalDivider(color = Color.LightGray, thickness = 1.dp)

            TextButton(
                onClick = {
                    showLoginDialog.value = true
                },
                modifier = Modifier.size(width = headerHeight, height = headerHeight)
            ) {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = null
                )
            }

            VerticalDivider(color = Color.LightGray, thickness = 1.dp)

            TextButton(
                onClick = {
                    showAllCookiesDialog.value = true
                },
                modifier = Modifier.size(width = headerHeight, height = headerHeight)
            ) {
                Image(
                    painter = painterResource(R.drawable.menu),
                    contentDescription = null
                )
            }
        }

        HorizontalDivider(color = jnsRed, thickness = 1.dp)
    }
}
