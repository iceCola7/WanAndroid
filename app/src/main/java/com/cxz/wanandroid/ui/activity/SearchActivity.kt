package com.cxz.wanandroid.ui.activity

import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.mvp.contract.SearchContract
import com.cxz.wanandroid.mvp.presenter.SearchPresenter
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : BaseActivity(), SearchContract.View {

    /**
     * Presenter
     */
    private val mPresenter by lazy {
        SearchPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)

        search_toolbar.run {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun start() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
    }

}
