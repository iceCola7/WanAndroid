package com.cxz.wanandroid.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.getAgentWeb
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.ContentContract
import com.cxz.wanandroid.mvp.presenter.ContentPresenter
import com.cxz.wanandroid.widget.NestedScrollAgentWebView
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class ContentActivity : BaseMvpSwipeBackActivity<ContentContract.View, ContentContract.Presenter>(), ContentContract.View {

    private val TAG = "ContentActivity"

    private var mAgentWeb: AgentWeb? = null

    private var shareTitle: String = ""
    private var shareUrl: String = ""
    private var shareId: Int = -1

    override fun createPresenter(): ContentContract.Presenter = ContentPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_content

    override fun initData() {}

    override fun initView() {
        super.initView()

        intent.extras?.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, -1)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY, "")
            shareUrl = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        toolbar.apply {
            title = ""//getString(R.string.loading)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tv_title.apply {
            text = getString(R.string.loading)
            visibility = View.VISIBLE
            postDelayed({
                tv_title.isSelected = true
            }, 2000)
        }

        initWebView()

    }

    /**
     * 初始化 WebView
     */
    private fun initWebView() {

        val webView = NestedScrollAgentWebView(this)

        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        mAgentWeb = shareUrl.getAgentWeb(
                this,
                cl_main,
                layoutParams,
                webView,
                mWebViewClient,
                mWebChromeClient,
                mThemeColor)

        mAgentWeb?.webCreator?.webView?.apply {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = false
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    override fun start() {}

    private val mWebViewClient = object : WebViewClient() {

        // 拦截的网址
        private val blackHostList = arrayListOf(
                "www.taobao.com",
                "www.jd.com",
                "yun.tuisnake.com",
                "yun.lvehaisen.com",
                "yun.tuitiger.com"
        )

        fun isBlackHost(host: String): Boolean {
            for (blackHost in blackHostList) {
                if (blackHost == host) {
                    return true
                }
            }
            return false
        }

        private fun shouldInterceptRequest(uri: Uri?): Boolean {
            if (uri != null) {
                val host = uri.host ?: ""
                return isBlackHost(host)
            }
            return false
        }

        private fun shouldOverrideUrlLoading(uri: Uri?): Boolean {
            if (uri != null) {
                val host = uri.host ?: ""
                return isBlackHost(host)
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            if (shouldInterceptRequest(request?.url)) {
                return WebResourceResponse(null, null, null)
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            if (shouldInterceptRequest(Uri.parse(url))) {
                return WebResourceResponse(null, null, null)
            }
            return super.shouldInterceptRequest(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return shouldOverrideUrlLoading(Uri.parse(url))
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return shouldOverrideUrlLoading(request?.url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loge(TAG, "onPageStarted---->>$url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            loge(TAG, "onPageFinished---->>$url")
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            // super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }

    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            tv_title?.text = title
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                            Intent.EXTRA_TEXT, getString(
                            R.string.share_article_url,
                            getString(R.string.app_name), shareTitle, shareUrl
                    )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like -> {
                if (isLogin) {
                    mPresenter?.addCollectArticle(shareId)
                } else {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showToast(resources.getString(R.string.login_tint))
                }
                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mAgentWeb?.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (mAgentWeb?.handleKeyEvent(keyCode, event)!!) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

}
