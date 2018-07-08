package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment

class SearchListFragment: BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): SearchListFragment{
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_search_list

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}