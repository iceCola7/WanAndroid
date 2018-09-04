package com.cxz.wanandroid.base

/**
 * Created by chenxz on 2018/4/21.
 */
interface IPresenter<in V : IView> {

    fun attachView(mView: V)

    fun detachView()

}