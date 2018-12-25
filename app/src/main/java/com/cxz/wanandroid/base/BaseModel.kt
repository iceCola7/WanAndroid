package com.cxz.wanandroid.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by chenxz on 2018/8/18.
 */
abstract class BaseModel : IModel, LifecycleObserver {

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun addDisposable(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable?.add(it) }
    }

    override fun onDetach() {
        unDispose()
    }

    private fun unDispose() {
        mCompositeDisposable?.clear()  // 保证Activity结束时取消
        mCompositeDisposable = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

}