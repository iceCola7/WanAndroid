package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.ProjectListContract
import com.cxz.wanandroid.mvp.model.ProjectListModel

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.View>(), ProjectListContract.Presenter {

    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun requestProjectList(page: Int, cid: Int) {
        if (page == 1)
            mView?.showLoading()
        val disposable = projectListModel.requestProjectList(page, cid)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setProjectList(results.data)
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