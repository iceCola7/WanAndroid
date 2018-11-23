package com.cxz.wanandroid.ext

import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.http.exception.ErrorStatus
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.model.bean.BaseBean
import com.cxz.wanandroid.rx.SchedulerUtils
import com.cxz.wanandroid.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author chenxz
 * @date 2018/11/22
 * @desc
 */

fun <T : BaseBean> Observable<T>.ss(
        model: IModel?,
        view: IView?,
        onSuccess: (T) -> Unit
) {
    this.compose(SchedulerUtils.ioToMain())
            .retryWhen(RetryWithDelay())
            .subscribe(object : Observer<T> {
                override fun onComplete() {
                    view?.hideLoading()
                }

                override fun onSubscribe(d: Disposable) {
                    view?.showLoading()
                    model?.addDisposable(d)
                    if (!NetWorkUtil.isNetworkConnected(App.instance)) {
                        view?.showDefaultMsg("当前网络不可用，请检查网络设置")
                        onComplete()
                    }
                }

                override fun onNext(t: T) {
                    when {
                        t.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(t)
                        t.errorCode == ErrorStatus.TOKEN_INVAILD -> {
                            // Token 过期，重新登录
                        }
                        else -> view?.showDefaultMsg(t.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    view?.hideLoading()
                    ExceptionHandle.handleException(t)
                }
            })
}

fun <T : BaseBean> Observable<T>.sss(
        view: IView?,
        onSuccess: (T) -> Unit
): Disposable {
    view?.showLoading()
    return this.compose(SchedulerUtils.ioToMain())
            .retryWhen(RetryWithDelay())
            .subscribe({
                when {
                    it.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(it)
                    it.errorCode == ErrorStatus.TOKEN_INVAILD -> {
                        // Token 过期，重新登录
                    }
                    else -> view?.showDefaultMsg(it.errorMsg)
                }
                view?.hideLoading()
            }, {
                view?.hideLoading()
                ExceptionHandle.handleException(it)
            })
}

