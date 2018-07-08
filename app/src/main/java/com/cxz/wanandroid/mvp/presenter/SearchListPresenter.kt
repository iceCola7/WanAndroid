package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.SearchListContract
import com.cxz.wanandroid.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.View>(), SearchListContract.Presenter {

    private val searchListModel: SearchListModel by lazy {
        SearchListModel()
    }

    override fun queryBySearchKey(page: Int, key: String) {
        if (page == 0)
            mRootView?.showLoading()
        val disposable = searchListModel.queryBySearchKey(page, key)
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showArticles(results.data)
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