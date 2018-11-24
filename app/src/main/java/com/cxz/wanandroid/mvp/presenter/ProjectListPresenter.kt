package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.ProjectListContract
import com.cxz.wanandroid.mvp.model.ProjectListModel

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.Model, ProjectListContract.View>(), ProjectListContract.Presenter {

    override fun createModel(): ProjectListContract.Model? = ProjectListModel()

    override fun requestProjectList(page: Int, cid: Int) {
        mModel?.requestProjectList(page, cid)?.ss(mModel, mView, page == 1) {
            mView?.setProjectList(it.data)
        }
    }

}