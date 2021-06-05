package com.cxz.wanandroid.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * @author chenxz
 * @date 2019/11/24
 * @desc ColorUtil
 */
object ColorUtil {

    /**
     * 计算颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    fun alphaColor(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 计算颜色
     *
     * @param color color值
     * @param alpha alpha值[0-1]
     * @return 最终的状态栏颜色
     */
    fun alphaColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return alphaColor(color, (alpha * 255).toInt())
    }

    /**
     * 根据fraction值来计算当前的颜色
     *
     * @param colorFrom 起始颜色
     * @param colorTo   结束颜色
     * @param fraction  变量
     * @return 当前颜色
     */
    fun changingColor(@ColorInt colorFrom: Int, @ColorInt colorTo: Int, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Int {
        val redStart = Color.red(colorFrom)
        val blueStart = Color.blue(colorFrom)
        val greenStart = Color.green(colorFrom)
        val alphaStart = Color.alpha(colorFrom)

        val redEnd = Color.red(colorTo)
        val blueEnd = Color.blue(colorTo)
        val greenEnd = Color.green(colorTo)
        val alphaEnd = Color.alpha(colorTo)

        val redDifference = redEnd - redStart
        val blueDifference = blueEnd - blueStart
        val greenDifference = greenEnd - greenStart
        val alphaDifference = alphaEnd - alphaStart

        val redCurrent = (redStart + fraction * redDifference).toInt()
        val blueCurrent = (blueStart + fraction * blueDifference).toInt()
        val greenCurrent = (greenStart + fraction * greenDifference).toInt()
        val alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }

}