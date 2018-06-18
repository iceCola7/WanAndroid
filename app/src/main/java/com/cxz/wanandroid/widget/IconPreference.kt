package com.cxz.wanandroid.widget

import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import android.view.View
import com.afollestad.materialdialogs.color.CircleView
import com.cxz.wanandroid.R
import com.cxz.wanandroid.utils.SettingUtil

/**
 * Created by chenxz on 2018/6/13.
 */
class IconPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private var circleImageView: CircleView? = null

    init {

        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        val color = SettingUtil.getColor()
        circleImageView = view.findViewById(R.id.iv_preview)
        circleImageView!!.setBackgroundColor(color)
    }

    fun setView() {
        val color = SettingUtil.getColor()
        circleImageView!!.setBackgroundColor(color)
    }
}