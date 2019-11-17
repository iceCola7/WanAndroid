package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.SystemContract
import com.cxz.wanandroid.mvp.model.SystemModel

/**
 * @author chenxz
 * @date 2019/11/17
 * @desc
 */
class SystemPresenter : BasePresenter<SystemModel, SystemContract.View>(),
    SystemContract.Presenter {

    override fun createModel(): SystemModel? = SystemModel()

}