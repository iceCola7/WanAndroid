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

    /**
     * 设置夜间模式
     */
    fun setIsNightMode(flag: Boolean) {
        setting.edit().putBoolean("switch_nightMode", flag).apply()
    }

    /**
     * 获取是否开启夜间模式
     */
    fun getIsNightMode(): Boolean {
        return setting.getBoolean("switch_nightMode", false)
    }

    /**
     * 获取是否开启自动切换夜间模式
     */
    fun getIsAutoNightMode(): Boolean {
        return setting.getBoolean("auto_nightMode", false)
    }

    fun getNightStartHour(): String {
        return setting.getString("night_startHour", "22")
    }

    fun setNightStartHour(nightStartHour: String) {
        setting.edit().putString("night_startHour", nightStartHour).apply()
    }

    fun getNightStartMinute(): String {
        return setting.getString("night_startMinute", "00")
    }

    fun setNightStartMinute(nightStartMinute: String) {
        setting.edit().putString("night_startMinute", nightStartMinute).apply()
    }

    fun getDayStartHour(): String {
        return setting.getString("day_startHour", "06")
    }

    fun setDayStartHour(dayStartHour: String) {
        setting.edit().putString("day_startHour", dayStartHour).apply()
    }

    fun getDayStartMinute(): String {
        return setting.getString("day_startMinute", "00")
    }

    fun setDayStartMinute(dayStartMinute: String) {
        setting.edit().putString("day_startMinute", dayStartMinute).apply()
    }


}