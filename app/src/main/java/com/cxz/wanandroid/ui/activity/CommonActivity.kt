package com.cxz.wanandroid.ui.activity

import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ui.fragment.AboutFragment
import com.cxz.wanandroid.ui.fragment.CollectFragment
import com.cxz.wanandroid.ui.fragment.SearchListFragment
import com.cxz.wanandroid.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.toolbar.*

class CommonActivity : BaseActivity() {

    override fun attachLayoutRes(): Int = R.layout.activity_common

    override fun initData() {
    }

    override fun initView() {
        val type = intent.extras.getString(Constant.TYPE_KEY, "")
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val fragment = when (type) {
            Constant.Type.COLLECT_TYPE_KEY -> {
                toolbar.title = getString(R.string.collect)
                CollectFragment.getInstance(intent.extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY -> {
                toolbar.title = getString(R.string.about_us)
                AboutFragment.getInstance(intent.extras)
            }
            Constant.Type.SETTING_TYPE_KEY -> {
                toolbar.title = getString(R.string.setting)
                SettingFragment.getInstance(intent.extras)
            }
            Constant.Type.SEARCH_TYPE_KEY -> {
                toolbar.title = intent.extras.getString(Constant.SEARCH_KEY, "")
                SearchListFragment.getInstance(intent.extras)
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

}
