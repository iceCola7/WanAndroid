package com.cxz.wanandroid.http.cookies

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.cxz.wanandroid.app.App
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by chenxz on 2018/6/6.
 */
class PersistentCookieStore {

    private val LOG_TAG: String = "PersistentCookieStore"
    private val COOKIE_PREFS: String = "Cookies_Prefs"

    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>> = HashMap()

    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = App.context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        var prefsMap = cookiePrefs.all


        for (entry in prefsMap) {
            val cookieNames = TextUtils.split(entry.value as String, ",")
            for (name in cookieNames) {
                val encodedCookie = cookiePrefs.getString(name, null)
                if (encodedCookie != null) {
                    val decodedCookie = decodeCookie(encodedCookie)
                    if (decodedCookie != null) {
                        if (!cookies.containsKey(entry.key)) {
                            cookies.put(entry.key, ConcurrentHashMap())
                        }
                        cookies[entry.key]?.put(name, decodedCookie)
                    }
                }
            }
        }

    }

    private fun getCookieToken(cookie: Cookie): String = cookie.name() + "@" + cookie.domain()

    fun add(url: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)
        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (!cookie.persistent()) {
            if (!cookies.containsKey(url.host())) {
                cookies.put(url.host(), ConcurrentHashMap(10))
            }
            cookies[url.host()]?.put(name, cookie)
        } else {
            if (cookies.containsKey(url.host())) {
                cookies[url.host()]?.remove(name)
            }
        }
        // 将Cookie持久化到本地
        val prefsWriter: SharedPreferences.Editor = cookiePrefs.edit()
        prefsWriter.putString(url.host(), TextUtils.join(",", cookies[url.host()]?.entries))
        prefsWriter.putString(name, encodeCookie(OkHttpCookies(cookie)))
        prefsWriter.apply()
    }

    fun get(url: HttpUrl): List<Cookie> {
        val list: ArrayList<Cookie> = ArrayList()
        if (cookies.containsKey(url.host())) {
            list.addAll(cookies[url.host()]?.values!!)
        }
        return list
    }

    fun removeAll() {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        for (key in cookies.keys) {
            cookies.remove(key)
        }
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)
        return if (cookies.containsKey(url.host()) && cookies[url.host()]?.containsKey(name)!!) {
            cookies[url.host()]?.remove(name)

            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name)
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies[url.host()]?.keys))
            prefsWriter.apply()

            true
        } else {
            false
        }
    }

    fun getCookies(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys) {
            ret.addAll(cookies[key]?.values!!)
        }
        return ret
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    private fun encodeCookie(cookie: OkHttpCookies?): String? {
        if (cookie == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e)
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as OkHttpCookies).getCookies()
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e)
        }

        return cookie
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

}