package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

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

    interface Model : CommonContract.Model {

        fun requestProjectList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>>

    }

}