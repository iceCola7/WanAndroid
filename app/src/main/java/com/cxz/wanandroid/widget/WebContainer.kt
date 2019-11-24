package com.cxz.wanandroid.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.cxz.wanandroid.R
import com.cxz.wanandroid.utils.ColorUtil
import com.cxz.wanandroid.utils.SettingUtil

/**
 * @author chenxz
 * @date 2019/11/24
 * @desc WebContainer
 */
class WebContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CoordinatorLayout(context, attrs, defStyleAttr) {

    private var mDarkTheme: Boolean = false

    private var mMaskColor = Color.TRANSPARENT

    init {
        mDarkTheme = SettingUtil.getIsNightMode()
        if (mDarkTheme) {
            mMaskColor = ColorUtil.alphaColor(ContextCompat.getColor(getContext(), R.color.mask_color), 0.6f)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mDarkTheme) {
            canvas.drawColor(mMaskColor)
        }
    }

}
