package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.SearchListContract
import com.cxz.wanandroid.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.Model, SearchListContract.View>(), SearchListContract.Presenter {

    override fun createModel(): SearchListContract.Model? = SearchListModel()

    override fun queryBySearchKey(page: Int, key: String) {
        if (page == 0)
            mView?.showLoading()
        val disposable = mModel?.queryBySearchKey(page, key)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
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