package com.cxz.wanandroid.ext

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import com.cxz.wanandroid.R
import com.cxz.wanandroid.app.App
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.just.agentweb.DefaultWebClient

/**
 * Created by chenxz on 2018/4/22.
 */
/**
 * Log
 */
fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

fun Fragment.showToast(content: String): Toast {
    val toast = Toast.makeText(this.activity?.applicationContext, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun Context.showToast(content: String): Toast {
    val toast = Toast.makeText(App.context, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

/**
 * getAgentWeb
 */
fun String.getAgentWeb(
        activity: Activity, webContent: ViewGroup,
        layoutParams: ViewGroup.LayoutParams,
        receivedTitleCallback: ChromeClientCallbackManager.ReceivedTitleCallback?,
        webChromeClient: WebChromeClient?,
        webViewClient: WebViewClient
) = AgentWeb.with(activity)//传入Activity or Fragment
        .setAgentWebParent(webContent, layoutParams)//传入AgentWeb 的父控件
        .useDefaultIndicator()// 使用默认进度条
        .defaultProgressBarColor() // 使用默认进度条颜色
        .setWebChromeClient(webChromeClient)
        .setWebViewClient(webViewClient)
        .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
        .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
        .createAgentWeb()//
        .ready()
        .go(this)!!