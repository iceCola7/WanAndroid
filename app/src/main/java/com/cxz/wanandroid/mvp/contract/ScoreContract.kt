package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.BaseListResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.UserScoreBean
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
interface ScoreContract {

    interface View : IView {
        fun showUserScoreList(body: BaseListResponseBody<UserScoreBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getUserScoreList(page: Int)
    }

    interface Model : IModel {
        fun getUserScoreList(page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>>
    }

}