package com.cxz.wanandroid.base

import android.os.Bundle

/**
 * @author chenxz
 * @date 2018/9/7
 * @desc BaseMvpActivity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity<in V : IView, P : IPresenter<V>> : BaseActivity(), IView {

    /**
     * Presenter
     */
    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        if (mPresenter != null) {
            mPresenter?.attachView(this as V)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.detachView()
        }
        this.mPresenter = null
    }

}