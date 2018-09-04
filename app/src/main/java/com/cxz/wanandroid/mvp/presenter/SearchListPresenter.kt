package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.SearchListContract
import com.cxz.wanandroid.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.View>(), SearchListContract.Presenter {

    private val searchListModel: SearchListModel by lazy {
        SearchListModel()
    }

    override fun queryBySearchKey(page: Int, key: String) {
        if (page == 0)
            mView?.showLoading()
        val disposable = searchListModel.queryBySearchKey(page, key)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showArticles(results.data)
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