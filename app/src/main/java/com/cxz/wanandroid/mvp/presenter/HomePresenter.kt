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
        if (num == 0)
            mRootView?.showLoading()
        val disposable = homeModel.requestArticles(num)
                .subscribe({ results ->
                    mRootView?.apply {
                        hideLoading()
                        if (num == 0)
                            setArticles(results.data)
                        else
                            setMoreArticles(results.data)
                    }
                })
        addSubscription(disposable)
    }

}