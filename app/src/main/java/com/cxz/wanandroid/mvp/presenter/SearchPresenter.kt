package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.SearchContract
import com.cxz.wanandroid.mvp.model.SearchModel

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val searchModel by lazy {
        SearchModel()
    }

    override fun getHotSearchData() {
        mRootView?.showLoading()
        val disposable = searchModel.getHotSearchData()
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showHotSearchData(results.data)
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