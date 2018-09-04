package com.cxz.wanandroid.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Created by chenxz on 2018/4/21.
 */
abstract class BasePresenter<V : IView> : IPresenter<V>, LifecycleObserver {

    var mView: V? = null
        private set

    private val isViewAttached: Boolean
        get() = mView != null

    private var mCompositeDisposable = CompositeDisposable()

    open fun useEventBus(): Boolean = false

    override fun attachView(mView: V) {
        this.mView = mView
        if (mView is LifecycleOwner) {
            (mView as LifecycleOwner).lifecycle.addObserver(this)
        }
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun detachView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        // 保证activity结束时取消所有正在执行的订阅
        unDispose()
        mView = null
    }

    open fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    open fun addSubscription(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    private fun unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear()  // 保证Activity结束时取消
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        detachView()
        owner.lifecycle.removeObserver(this)
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")

}