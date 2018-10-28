package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.WeChatContract
import com.cxz.wanandroid.mvp.model.WeChatModel

/**
 * @author chenxz
 * @date 2018/10/28
 * @desc
 */
class WeChatPresenter : BasePresenter<WeChatContract.View>(), WeChatContract.Presenter {

    private val weChatModel: WeChatModel by lazy {
        WeChatModel()
    }

    override fun getWXChapters() {
        mView?.showLoading()
        val disposable = weChatModel.getWXChapters()
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showWXChapters(results.data)
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