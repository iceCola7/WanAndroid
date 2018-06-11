package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
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
            mRootView?.showLoading()
        val disposable = projectListModel.requestProjectList(page, cid)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setProjectList(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}