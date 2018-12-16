package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.CollectionArticle
import com.cxz.wanandroid.mvp.model.bean.CollectionResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/6/9.
 */
interface CollectContract {

    interface View : IView {

        fun setCollectList(articles: CollectionResponseBody<CollectionArticle>)

        fun showRemoveCollectSuccess(success: Boolean)

        fun scrollToTop()

    }

    interface Presenter : IPresenter<View> {

        fun getCollectList(page: Int)

        fun removeCollectArticle(id: Int, originId: Int)

    }

    interface Model : IModel {

        fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>>

        fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>>

    }

}