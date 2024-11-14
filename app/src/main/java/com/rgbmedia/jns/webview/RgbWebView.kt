package com.rgbmedia.jns.webview

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.viewinterop.AndroidView
import com.rgbmedia.jns.utils.RgbConstants


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RgbWebView(url: MutableState<String>, showJavascriptDialog: MutableState<Boolean>) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = RgbWebViewClient()
            webChromeClient = RgbWebChromeClient()

            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true

            settings.userAgentString += " rgbmedia-app android app"

            addJavascriptInterface(WebAppInterface(showJavascriptDialog), "Android")
        }
    }, update = {
        Log.d(RgbConstants.GENERAL_DEBUG_TAG, "update")
        it.loadUrl(url.value)
    })
}

class RgbWebViewClient : WebViewClient() {
}

class RgbWebChromeClient : WebChromeClient() {
}

class WebAppInterface(val showJavascriptDialog: MutableState<Boolean>) {
    @JavascriptInterface
    fun openNativeLoginDialog() {
        Log.d(RgbConstants.GENERAL_DEBUG_TAG, "openNativeLoginDialog")
        showJavascriptDialog.value = true
    }
}
