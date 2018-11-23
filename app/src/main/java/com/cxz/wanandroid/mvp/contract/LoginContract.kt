package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/5/27.
 */
interface LoginContract {

    interface View : IView {

        fun loginSuccess(data: LoginData)

        fun loginFail()

    }

    interface Presenter : IPresenter<View> {

        fun loginWanAndroid(username: String, password: String)

    }

    interface Model : IModel {

        fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>>

    }

}