package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.sss
import com.cxz.wanandroid.mvp.contract.ProjectListContract
import com.cxz.wanandroid.mvp.model.ProjectListModel

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.Model, ProjectListContract.View>(), ProjectListContract.Presenter {

    override fun createModel(): ProjectListContract.Model? = ProjectListModel()

    override fun requestProjectList(page: Int, cid: Int) {
        if (page == 1)
            mView?.showLoading()
        addDisposable(
                mModel?.requestProjectList(page, cid)?.sss(mView) {
                    mView?.setProjectList(it.data)
                }
        )
    }

}