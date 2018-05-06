package com.cxz.wanandroid.ext

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.cxz.wanandroid.app.App

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