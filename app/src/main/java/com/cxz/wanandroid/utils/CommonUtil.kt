package com.cxz.wanandroid.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field
import java.util.*


/**
 * Created by chenxz on 2018/5/14.
 */
object CommonUtil {

    init {
    }

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = random.nextInt(150)
        //0-190
        val green = random.nextInt(150)
        //0-190
        val blue = random.nextInt(150)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }

    /**
     * 解决InputMethodManager引起的内存泄漏
     * 在Activity的onDestroy方法里调用
     */
    fun fixInputMethodManagerLeak(context: Context) {

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var field: Field? = null
        var objGet: Any? = null
        for (i in arr.indices) {
            val param = arr[i]
            try {
                field = imm.javaClass.getDeclaredField(param)
                if (field.isAccessible === false) {
                    field.isAccessible = true
                }
                objGet = field.get(imm)
                if (objGet != null && objGet is View) {
                    val view = objGet
                    if (view.context === context) {
                        // 被InputMethodManager持有引用的context是想要目标销毁的
                        field.set(imm, null) // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }

    }

}