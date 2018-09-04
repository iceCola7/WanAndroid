package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
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
        mView?.showLoading()
        val disposable = projectModel.requestProjectTree()
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setProjectTree(results.data)
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