package com.cxz.wanandroid.ui.setting

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.cxz.wanandroid.R
import com.cxz.wanandroid.utils.SettingUtil

/**
 * Created by chenxz on 2018/6/13.
 */
class AutoNightModeFragment : PreferenceFragment() {

    private lateinit var autoNight: Preference
    private lateinit var autoDay: Preference

    private lateinit var nightStartHour: String
    private lateinit var nightStartMinute: String
    private lateinit var dayStartHour: String
    private lateinit var dayStartMinute: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_autonight)
        setHasOptionsMenu(true)

        autoNight = findPreference("auto_night")
        autoDay = findPreference("auto_day")

        setDefaultText()

        autoNight.setOnPreferenceClickListener {
            val dialog = TimePickerDialog(activity, { _, hour, minute ->
                SettingUtil.setNightStartHour(if (hour > 9) hour.toString() else "0$hour")
                SettingUtil.setNightStartMinute(if (minute > 9) minute.toString() else "0$minute")
                setDefaultText()
            }, nightStartHour.toInt(), nightStartMinute.toInt(), true)
            dialog.show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done)
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel)
            false
        }

        autoDay.setOnPreferenceClickListener {
            val dialog = TimePickerDialog(activity, { _, hour, minute ->
                SettingUtil.setDayStartHour(if (hour > 9) hour.toString() else "0$hour")
                SettingUtil.setDayStartMinute(if (minute > 9) minute.toString() else "0$minute")
                setDefaultText()
            }, dayStartHour.toInt(), dayStartMinute.toInt(), true)
            dialog.show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done)
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel)
            false
        }

    }

    private fun setDefaultText() {

        nightStartHour = SettingUtil.getNightStartHour()
        nightStartMinute = SettingUtil.getNightStartMinute()
        dayStartHour = SettingUtil.getDayStartHour()
        dayStartMinute = SettingUtil.getDayStartMinute()

        autoNight.summary = "$nightStartHour:$nightStartMinute"
        autoDay.summary = "$dayStartHour:$dayStartMinute"
    }

}