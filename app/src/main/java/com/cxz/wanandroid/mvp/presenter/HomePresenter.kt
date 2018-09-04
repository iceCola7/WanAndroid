package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.HomeModel
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/4/21.
 */
class HomePresenter : CommonPresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestBanner() {
        val disposable = homeModel.requestBanner()
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        setBanner(results.data)
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    override fun requestArticles(num: Int) {
        if (num == 0)
            mView?.showLoading()
        val disposable = homeModel.requestArticles(num)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setArticles(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    override fun requestHomeData() {
        mView?.showLoading()
        val disposable = Observable
                .merge(homeModel.requestArticles(0), homeModel.requestBanner())
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            if (results.data is ArticleResponseBody) {
                                setArticles(results.data)
                            } else {
                                setBanner(results.data as List<Banner>)
                            }
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}