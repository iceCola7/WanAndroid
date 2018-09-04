package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.MainContract
import com.cxz.wanandroid.mvp.model.MainModel

/**
 * @author chenxz
 * @date 2018/8/30
 * @desc
 */
class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val mainModel: MainModel by lazy {
        MainModel()
    }

    override fun logout() {
        mView?.showLoading()
        val disposable = mainModel.logout()
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showLogoutSuccess(success = true)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }
}