package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.HomeModel

/**
 * Created by chenxz on 2018/4/21.
 */
class HomePresenter : CommonPresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestBanner() {
        val disposable = homeModel.requestBanner()
                .subscribe({ results ->
                    mRootView?.apply {
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

}