package com.cxz.wanandroid.ui.fragment

import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.presenter.AddTodoPresenter

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoFragment : BaseFragment(), AddTodoContract.View {

    private val mPresenter by lazy {
        AddTodoPresenter()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_add_todo

    override fun initView() {
        mPresenter.attachView(this)
    }

    override fun lazyLoad() {
    }

    override fun showAddTodo(success: Boolean) {


    }

    override fun showUpdateTodo(success: Boolean) {


    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}