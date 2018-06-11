package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.LoginContract
import com.cxz.wanandroid.mvp.model.LoginModel

/**
 * Created by chenxz on 2018/5/27.
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun loginWanAndroid(username: String, password: String) {
        mRootView?.showLoading()
        val disposable = loginModel.loginWanAndroid(username, password)
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                            loginFail()
                        } else {
                            loginSuccess(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}