package com.cxz.wanandroid.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.cxz.wanandroid.R

/**
 * Created by chenxz on 2018/6/9.
 * 自定义Toast
 */
class CustomToast {

    private var toast: Toast
    private var textView: TextView

    constructor(context: Context?, message: String) : this(context, message, Toast.LENGTH_SHORT)

    constructor(context: Context?, message: String, duration: Int) {
        toast = Toast(context)
        toast.duration = duration
        val view = View.inflate(context, R.layout.toast_custom, null)
        textView = view.findViewById(R.id.tv_prompt)
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
    }

    fun show() {
        toast.show()
    }

}