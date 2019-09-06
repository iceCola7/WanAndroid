package com.cxz.wanandroid.ui.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created by chenxz on 2018/6/10.
 */
class AboutFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): AboutFragment {
            val fragment = AboutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(view: View) {
        about_content.run {
            text = Html.fromHtml(getString(R.string.about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }

        val versionStr = getString(R.string.app_name) + " V" + activity?.packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName
        about_version.text = versionStr

        setLogoBg()

    }

    private fun setLogoBg() {
        val drawable = iv_logo.background as GradientDrawable
        drawable.setColor(SettingUtil.getColor())
        iv_logo.setBackgroundDrawable(drawable)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_about

    override fun lazyLoad() {
    }
}