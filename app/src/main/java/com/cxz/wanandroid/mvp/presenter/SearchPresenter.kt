package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.SearchContract
import com.cxz.wanandroid.mvp.model.SearchModel

class SearchPresenter: BasePresenter<SearchContract.View>(),SearchContract.Presenter {

    private val searchModel by lazy {
        SearchModel()
    }

}