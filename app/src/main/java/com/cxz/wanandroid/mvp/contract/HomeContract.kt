package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner

/**
 * Created by chenxz on 2018/4/21.
 */
interface HomeContract {

    interface View : IView {

        fun setBanner(banners: List<Banner>)

        fun setArticles(articles: ArticleResponseBody)

        fun setMoreArticles(articles: ArticleResponseBody)

    }

    interface Presenter : IPresenter<View> {

        fun requestBanner()

        fun requestArticles(num: Int)

    }

}