package com.cxz.wanandroid.ui.fragment

import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment

/**
 * @author chenxz
 * @date 2019/11/17
 * @desc 扫码下载
 */
class QrCodeFragment : BaseFragment() {

    companion object {
        fun getInstance(): QrCodeFragment = QrCodeFragment()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_qr_code

    override fun initView(view: View) {
    }

    override fun lazyLoad() {
    }
}