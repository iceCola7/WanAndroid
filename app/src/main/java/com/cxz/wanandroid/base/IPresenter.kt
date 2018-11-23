package com.cxz.wanandroid.base

/**
 * Created by chenxz on 2018/4/21.
 */
interface IPresenter<in V : IView> {

    /**
     * 绑定 View
     */
    fun attachView(mView: V)

    /**
     * 解绑 View
     */
    fun detachView()

}