package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * Created by chenxz on 2018/5/20.
 */
interface ProjectListContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setProjectList(articles: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestProjectList(page: Int, cid: Int)

    }

}