package com.cxz.wanandroid.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by chenxz on 2018/4/21.
 */
open class BasePresenter<V : IView> : IPresenter<V>, LifecycleObserver {

    var mRootView: V? = null
        private set

    private var compositeDisposable = CompositeDisposable()

    override fun attachView(mRootView: V) {
        this.mRootView = mRootView
        if (mRootView is LifecycleOwner) {
            (mRootView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mRootView = null
        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    private val isViewAttached: Boolean
        get() = mRootView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        detachView()
        owner.lifecycle.removeObserver(this)
    }

}