package com.cxz.wanandroid.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.ext.getAgentWeb
import com.cxz.wanandroid.utils.StatusBarUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import kotlinx.android.synthetic.main.container.*
import kotlinx.android.synthetic.main.toolbar.*

class ContentActivity : BaseActivity() {

    private lateinit var agentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId: Int = 0

    override fun attachLayoutRes(): Int = R.layout.activity_content

    override fun initData() {
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.loading)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            StatusBarUtil.setPaddingSmart(this@ContentActivity, toolbar)
        }
        intent.extras.let {
            shareId = it.getInt(Contanst.CONTENT_ID_KEY)
            shareTitle = it.getString(Contanst.CONTENT_TITLE_KEY)
            shareUrl = it.getString(Contanst.CONTENT_URL_KEY)
        }
        agentWeb = shareUrl.getAgentWeb(this, container,
                LinearLayout.LayoutParams(-1, -1),
                receivedTitleCallback,
                webChromeClient, webViewClient)

    }

    override fun start() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                            getString(
                                    R.string.share_article_url,
                                    getString(R.string.app_name),
                                    shareTitle,
                                    shareUrl
                            ))
                    type = Contanst.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like -> {

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        return if (agentWeb?.handleKeyEvent(keyCode, event)!!) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    /**
     * receivedTitleCallback
     */
    private val receivedTitleCallback =
            ChromeClientCallbackManager.ReceivedTitleCallback { _, title ->
                title?.let {
                    toolbar.title = it
                }
            }

    /**
     * webViewClient
     */
    private val webViewClient = object : WebViewClient() {
    }

    /**
     * webChromeClient
     */
    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
        }
    }

}
