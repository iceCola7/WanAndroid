package com.cxz.wanandroid.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup

/**
 * Created by chenxz on 2018/5/21.
 */
class StatusBarCompat {

    companion object {

        var statusColor: Int = Color.parseColor("#C2185B")

        fun compat(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                var contentView: ViewGroup = activity.findViewById(android.R.id.content)
                var statusBarView: View = contentView.getChildAt(0)
                // 改变颜色时避免重复添加statusBarView
                if (statusBarView != null && statusBarView.measuredHeight == getStatusBarHeight(activity)) {
                    statusBarView.setBackgroundColor(statusColor)
                    return
                }
                statusBarView = View(activity)
                var lp: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity))
                statusBarView.setBackgroundColor(statusColor)
                contentView.addView(statusBarView, lp)
            }

        }

        /**
         * 得到状态栏的高度
         */
        fun getStatusBarHeight(context: Context): Int {
            var result = 24
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    }

}