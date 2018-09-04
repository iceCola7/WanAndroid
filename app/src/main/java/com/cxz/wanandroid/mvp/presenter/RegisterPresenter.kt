package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.RegisterContract
import com.cxz.wanandroid.mvp.model.RegisterModel

/**
 * Created by chenxz on 2018/6/3.
 */
class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    private val registerModel by lazy {
        RegisterModel()
    }

    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        mView?.showLoading()
        val disposable = registerModel.registerWanAndroid(username, password, repassword)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                            registerFail()
                        } else {
                            registerSuccess(results.data)
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