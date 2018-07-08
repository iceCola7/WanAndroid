package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.HotSearchBean

interface SearchContract {

    interface View : IView {

        fun showHotSearchData(hotSearchDatas: List<HotSearchBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getHotSearchData()

    }

}