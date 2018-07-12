package com.cxz.wanandroid.utils

import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * Created by chenxz on 2018/4/21.
 */
object RomUtil {

    internal object AvailableRomType {
        val MIUI = 1
        val FLYME = 2
        val ANDROID_NATIVE = 3
        val NA = 4
    }

    fun isLightStatusBarAvailable(): Boolean {
        return isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove()
    }

    fun getLightStatausBarAvailableRomType(): Int {
        if (isMIUIV6OrAbove()) {
            return AvailableRomType.MIUI
        }

        if (isFlymeV4OrAbove()) {
            return AvailableRomType.FLYME
        }

        return if (isAndroidMOrAbove()) {
            AvailableRomType.ANDROID_NATIVE
        } else AvailableRomType.NA

    }

    //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
    //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
    private fun isFlymeV4OrAbove(): Boolean {
        val displayId = Build.DISPLAY
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            val displayIdArray = displayId.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (temp in displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*".toRegex())) {
                    return true
                }
            }
        }
        return false
    }

    //MIUI V6对应的versionCode是4
    //MIUI V7对应的versionCode是5
    private fun isMIUIV6OrAbove(): Boolean {
        val miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code")
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                val miuiVersionCode = Integer.parseInt(miuiVersionCodeStr)
                if (miuiVersionCode >= 4) {
                    return true
                }
            } catch (e: Exception) {
            }

        }
        return false
    }

    //Android Api 23以上
    private fun isAndroidMOrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop " + propName)
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input!!.readLine()
            input!!.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input!!.close()
                } catch (e: IOException) {
                }

            }
        }
        return line
    }

}