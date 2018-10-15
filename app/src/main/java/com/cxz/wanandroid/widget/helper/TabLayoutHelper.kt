package com.cxz.wanandroid.widget.helper

import android.os.Build
import android.support.design.widget.TabLayout
import android.widget.LinearLayout
import com.cxz.wanandroid.utils.DisplayManager
import java.lang.reflect.Field

/**
 * Created by chenxz on 2018/5/27.
 */
object TabLayoutHelper {

    fun setUpIndicatorWidth(tabLayout: TabLayout) {

        val tabLayoutClass = tabLayout.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip")
            tabStrip!!.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        var layout: LinearLayout? = null
        try {
            if (tabStrip != null) {
                layout = tabStrip.get(tabLayout) as LinearLayout
            }
            for (i in 0 until layout!!.childCount) {
                val child = layout.getChildAt(i)
                child.setPadding(0, 0, 0, 0)
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.marginStart = DisplayManager.dip2px(50f)!!
                    params.marginEnd = DisplayManager.dip2px(50f)!!
                }
                child.layoutParams = params
                child.invalidate()
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

}