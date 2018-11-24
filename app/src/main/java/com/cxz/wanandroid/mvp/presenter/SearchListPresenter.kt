package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.SearchListContract
import com.cxz.wanandroid.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.Model, SearchListContract.View>(), SearchListContract.Presenter {

    override fun createModel(): SearchListContract.Model? = SearchListModel()

    override fun queryBySearchKey(page: Int, key: String) {
        mModel?.queryBySearchKey(page, key)?.ss(mModel, mView, page == 0) {
            mView?.showArticles(it.data)
        }
    }

}