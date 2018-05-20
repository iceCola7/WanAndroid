package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * Created by chenxz on 2018/5/20.
 */
interface ProjectListContract {

    interface View : IView {

        fun scrollToTop()

        fun setProjectList(articles: ArticleResponseBody)

    }

    interface Presenter : IPresenter<View> {

        fun requestProjectList(page: Int, cid: Int)

    }

}