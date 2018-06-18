package com.cxz.wanandroid.utils

import android.graphics.Color
import android.preference.PreferenceManager
import com.cxz.wanandroid.R
import com.cxz.wanandroid.app.App

/**
 * Created by chenxz on 2018/6/18.
 */
object SettingUtil {
    private val setting = PreferenceManager.getDefaultSharedPreferences(App.context)

    /**
     * 获取是否开启无图模式
     */
    fun getIsNoPhotoMode(): Boolean {
        return setting.getBoolean("switch_noPhotoMode", false) && NetWorkUtil.isMobile(App.context)
    }

    /**
     * 获取主题颜色
     */
    fun getColor(): Int {
        val defaultColor = App.context.resources.getColor(R.color.colorPrimary)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else color
    }

    /**
     * 设置主题颜色
     */
    fun setColor(color: Int) {
        setting.edit().putInt("color", color).apply()
    }


    /**
     * 获取是否开启导航栏上色
     */
    fun getNavBar(): Boolean {
        return setting.getBoolean("nav_bar", false)
    }


}