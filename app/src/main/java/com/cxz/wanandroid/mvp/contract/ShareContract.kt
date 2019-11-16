package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.ShareResponseBody
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
interface ShareContract {

    interface View : CommonContract.View {
        fun showShareList(body: ShareResponseBody)
        fun showDeleteArticle(success: Boolean)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun getShareList(page: Int)
        fun deleteShareArticle(id: Int)
    }

    interface Model : CommonContract.Model {
        fun getShareList(page: Int): Observable<HttpResult<ShareResponseBody>>
        fun deleteShareArticle(id: Int): Observable<HttpResult<Any>>
    }

}