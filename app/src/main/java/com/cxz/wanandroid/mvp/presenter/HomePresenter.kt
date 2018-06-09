package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.HomeModel

/**
 * Created by chenxz on 2018/4/21.
 */
class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestBanner() {

        mRootView?.showLoading()
        val disposable = homeModel.requestBanner()
                .subscribe({ results ->
                    mRootView?.apply {
                        hideLoading()
                        setBanner(results.data)
                    }
                })
        addSubscription(disposable)
    }

    override fun requestArticles(num: Int) {
        val disposable = homeModel.requestArticles(num)
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setArticles(results.data)
                        }
                        hideLoading()
                    }
                })
        addSubscription(disposable)
    }

    override fun addCollectArticle(id: Int) {
        val disposable = homeModel.addCollectArticle(id)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showCollectSuccess(true)
                        }
                    }
                })
        addSubscription(disposable)
    }

    override fun cancelCollectArticle(id: Int) {
        val disposable = homeModel.cancelCollectArticle(id)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showCancelCollectSuccess(true)
                        }
                    }
                })
        addSubscription(disposable)
    }

}