package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.NavigationContract
import com.cxz.wanandroid.mvp.model.NavigationModel

/**
 * Created by chenxz on 2018/5/13.
 */
class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {

    private val navigationModel by lazy {
        NavigationModel()
    }

    override fun requestNavigationList() {
        mRootView?.showLoading()
        val disposable = navigationModel.requestNavigationList()
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setNavigationData(results.data)
                        }
                    }
                })
        addSubscription(disposable)
    }

}