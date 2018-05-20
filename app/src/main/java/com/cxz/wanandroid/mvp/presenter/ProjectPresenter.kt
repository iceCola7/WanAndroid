package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.ProjectContract
import com.cxz.wanandroid.mvp.model.ProjectModel

/**
 * Created by chenxz on 2018/5/15.
 */
class ProjectPresenter : BasePresenter<ProjectContract.View>(), ProjectContract.Presenter {

    private val projectModel: ProjectModel by lazy {
        ProjectModel()
    }

    override fun requestProjectTree() {
        mRootView?.showLoading()
        val disposable = projectModel.requestProjectTree()
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setProjectTree(results.data)
                        }
                        hideLoading()
                    }
                })
        addSubscription(disposable)
    }

}