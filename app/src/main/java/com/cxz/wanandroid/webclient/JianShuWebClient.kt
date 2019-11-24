package com.cxz.wanandroid.webclient

import android.content.Context
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.cxz.wanandroid.utils.StringUtil
import com.cxz.wanandroid.utils.Wget
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

/**
 * @author chenxz
 * @date 2019/11/24
 * @desc JianShuWebClient  参考文章：https://mp.weixin.qq.com/s/gs2bojFLBB4IAWMyN9lfnw
 */
class JianShuWebClient : BaseWebClient() {

    private val rex = "(<style data-vue-ssr-id=[\\s\\S]*?>)([\\s\\S]*]?)(<\\/style>)"

    private val bodyRex = "<body class=\"([\\ss\\S]*?)\""

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        val urlStr = url ?: ""
        if (urlStr.startsWith(WebClientFactory.JIAN_SHU)) {
            val response = Wget.get(url ?: "")
            val res = darkBody(replaceCss(response, view!!.context))
            val input = ByteArrayInputStream(res.toByteArray())
            return WebResourceResponse("text/html", "utf-8", input)
        }
        return super.shouldInterceptRequest(view, url)
    }

    private fun darkBody(res: String): String {
        val pattern = Pattern.compile(bodyRex)
        val m = pattern.matcher(res)
        return res
        //return if (m.find()) {
        //    val s = "<body class=\"reader-night-mode normal-size\""
        //    res.replace(bodyRex.toRegex(), s)
        //} else res
    }

    private fun replaceCss(res: String, context: Context): String {
        val pattern = Pattern.compile(rex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val css = StringUtil.getString(context.assets.open("jianshu/jianshu.css"))
            val sb = StringBuilder()
            sb.append(m.group(1))
            sb.append(css)
            sb.append(m.group(3))
            val res = res.replace(rex.toRegex(), sb.toString())
            res
        } else {
            res
        }
    }

}