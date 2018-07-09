package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

interface SearchListContract {

    interface View : CommonContract.View {

        fun showArticles(articles: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun queryBySearchKey(page: Int, key: String)

    }

}