package com.cxz.wanandroid.base

/**
 * Created by chenxz on 2018/4/21.
 */
interface IView {

    fun showLoading()

    fun hideLoading()

    fun showError(errorMsg: String)

}