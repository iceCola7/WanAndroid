package com.cxz.wanandroid.http.cookies

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Created by chenxz on 2018/6/6.
 */
class CookieManager : CookieJar {

    private val COOKIE_STORE = PersistentCookieStore()

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        cookies ?: return
        url ?: return
        if (cookies.size > 0) {
            for (cookie in cookies) {
                COOKIE_STORE.add(url, cookie)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> = COOKIE_STORE.get(url)

    /**
     * 清除所有cookie
     */
    fun clearAllCookies() {
        COOKIE_STORE.removeAll()
    }

    /**
     * 清除指定cookie
     *
     * @param url HttpUrl
     * @param cookie Cookie
     * @return if clear cookies
     */
    fun clearCookies(url: HttpUrl, cookie: Cookie): Boolean {
        return COOKIE_STORE.remove(url, cookie)
    }

    /**
     * 获取cookies
     *
     * @return List<Cookie>
    </Cookie> */
    fun getCookies(): List<Cookie> {
        return COOKIE_STORE.getCookies()
    }
}