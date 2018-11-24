package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.NavigationContract
import com.cxz.wanandroid.mvp.model.NavigationModel

/**
 * Created by chenxz on 2018/5/13.
 */
class NavigationPresenter : BasePresenter<NavigationContract.Model, NavigationContract.View>(), NavigationContract.Presenter {

    override fun createModel(): NavigationContract.Model? = NavigationModel()

    override fun requestNavigationList() {
        mModel?.requestNavigationList()?.ss(mModel, mView) {
            mView?.setNavigationData(it.data)
        }
    }

}