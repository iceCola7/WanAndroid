package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant

/**
 * Created by chenxz on 2018/8/6.
 */

class TodoFragment : BaseFragment() {

    private var mType: Int = 0

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.TODO_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
    }

    override fun lazyLoad() {
    }
}