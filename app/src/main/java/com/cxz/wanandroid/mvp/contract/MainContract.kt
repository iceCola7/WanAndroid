package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2018/8/30
 * @desc
 */
interface MainContract {

    interface View : IView {
        fun showLogoutSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View> {

        fun logout()

    }

    interface Model : IModel {

        fun logout(): Observable<HttpResult<Any>>

    }

}