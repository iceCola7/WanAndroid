package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.presenter.TodoPresenter

/**
 * Created by chenxz on 2018/8/6.
 */

class TodoFragment : BaseFragment(), TodoContract.View {

    /**
     * Presenter
     */
    private val mPresenter by lazy {
        TodoPresenter()
    }

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

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
        mPresenter.attachView(this)
        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
    }

    override fun lazyLoad() {
        mPresenter.getTodoList(mType, 1)
    }
}