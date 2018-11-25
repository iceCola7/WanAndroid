package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment

/**
 * Created by chenxz on 2018/6/10.
 */
class SettingFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): SettingFragment {
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_setting

    override fun initView(view: View) {
    }

    override fun lazyLoad() {
    }
}