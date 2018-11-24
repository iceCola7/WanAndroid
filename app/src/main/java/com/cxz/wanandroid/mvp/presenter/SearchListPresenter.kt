package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.sss
import com.cxz.wanandroid.mvp.contract.SearchListContract
import com.cxz.wanandroid.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.Model, SearchListContract.View>(), SearchListContract.Presenter {

    override fun createModel(): SearchListContract.Model? = SearchListModel()

    override fun queryBySearchKey(page: Int, key: String) {
        if (page == 0)
            mView?.showLoading()
        addDisposable(
                mModel?.queryBySearchKey(page, key)?.sss(mView) {
                    mView?.showArticles(it.data)
                }
        )
    }

}