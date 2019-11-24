package com.cxz.wanandroid.webclient

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.*
import com.cxz.wanandroid.ext.loge

/**
 * @author chenxz
 * @date 2019/11/24
 * @desc BaseWebClient
 */
open class BaseWebClient : WebViewClient() {

    protected val TAG = "BaseWebClient"

    // 拦截的网址
    private val blackHostList = arrayListOf(
            "www.taobao.com",
            "www.jd.com",
            "yun.tuisnake.com",
            "yun.lvehaisen.com",
            "yun.tuitiger.com"
    )

    private fun isBlackHost(host: String): Boolean {
        for (blackHost in blackHostList) {
            if (blackHost == host) {
                return true
            }
        }
        return false
    }

    private fun shouldInterceptRequest(uri: Uri?): Boolean {
        if (uri != null) {
            val host = uri.host ?: ""
            return isBlackHost(host)
        }
        return false
    }

    private fun shouldOverrideUrlLoading(uri: Uri?): Boolean {
        if (uri != null) {
            val host = uri.host ?: ""
            return isBlackHost(host)
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        if (shouldInterceptRequest(request?.url)) {
            return WebResourceResponse(null, null, null)
        }
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        loge(TAG, "------------>>$url")
        if (shouldInterceptRequest(Uri.parse(url))) {
            return WebResourceResponse(null, null, null)
        }
        return super.shouldInterceptRequest(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return shouldOverrideUrlLoading(Uri.parse(url))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return shouldOverrideUrlLoading(request?.url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loge(TAG, "onPageStarted---->>$url")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        loge(TAG, "onPageFinished---->>$url")
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        // super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }

}