package com.cxz.wanandroid.ui.activity

import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseSwipeBackActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.ui.fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class CommonActivity : BaseSwipeBackActivity() {

    private var mType = ""

    override fun attachLayoutRes(): Int = R.layout.activity_common

    override fun initData() {
    }

    override fun initView() {
        val extras = intent.extras
        mType = extras.getString(Constant.TYPE_KEY, "")
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val fragment = when (mType) {
            Constant.Type.COLLECT_TYPE_KEY -> {
                toolbar.title = getString(R.string.collect)
                CollectFragment.getInstance(extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY -> {
                toolbar.title = getString(R.string.about_us)
                AboutFragment.getInstance(extras)
            }
            Constant.Type.SETTING_TYPE_KEY -> {
                toolbar.title = getString(R.string.setting)
                SettingFragment.getInstance(extras)
            }
            Constant.Type.SEARCH_TYPE_KEY -> {
                toolbar.title = extras.getString(Constant.SEARCH_KEY, "")
                SearchListFragment.getInstance(extras)
            }
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.add)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.edit)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.see)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SHARE_ARTICLE_TYPE_KEY -> {
                toolbar.title = getString(R.string.share_article)
                ShareArticleFragment.getInstance()
            }
            Constant.Type.SCAN_QR_CODE_TYPE_KEY->{
                toolbar.title = getString(R.string.scan_code_download)
                QrCodeFragment.getInstance()
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
                .commit()

    }

    override fun start() {
    }

    override fun initColor() {
        super.initColor()
        EventBus.getDefault().post(ColorEvent(true, mThemeColor))
    }

}
