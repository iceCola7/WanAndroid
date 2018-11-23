package com.cxz.wanandroid.base

import io.reactivex.disposables.Disposable

/**
 * Created by chenxz on 2018/8/18.
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}