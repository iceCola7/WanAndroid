package com.cxz.wanandroid.ext

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.TextView
import com.cxz.wanandroid.R
import com.cxz.wanandroid.widget.CustomToast
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.just.agentweb.DefaultWebClient
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chenxz on 2018/4/22.
 */
/**
 * Log
 */
fun loge(content: String?) {
    loge("CXZ", content)
}

fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

fun Fragment.showToast(content: String) {
    CustomToast(this?.activity?.applicationContext, content).show()
}

fun Context.showToast(content: String) {
    CustomToast(this, content).show()
}

fun Activity.showSnackMsg(msg: String) {
    val snackbar = Snackbar.make(this.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this, R.color.white))
    snackbar.show()
}

fun Fragment.showSnackMsg(msg: String) {
    this.activity ?: return
    val snackbar = Snackbar.make(this.activity!!.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this.activity!!, R.color.white))
    snackbar.show()
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

/**
 * 格式化当前日期
 */
fun formatCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date())
}
