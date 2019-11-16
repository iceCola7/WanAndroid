package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/16
 * @desc
 */
interface SquareContract {

    interface View : CommonContract.View {
        fun scrollToTop()
        fun showSquareList(body: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun getSquareList(page: Int)
    }

    interface Model : CommonContract.Model {
        fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>>
    }

}