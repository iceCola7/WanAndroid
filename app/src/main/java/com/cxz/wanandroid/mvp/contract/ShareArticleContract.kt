package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
interface ShareArticleContract {

    interface View : IView {
        fun getArticleTitle(): String
        fun getArticleLink(): String

        fun showShareArticle(success: Boolean)
    }

    interface Presenter : IPresenter<View> {
        fun shareArticle()
    }

    interface Model : IModel {
        fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>>
    }

}