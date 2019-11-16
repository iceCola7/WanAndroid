package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.ShareContract
import com.cxz.wanandroid.mvp.model.ShareModel

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
class SharePresenter : CommonPresenter<ShareModel, ShareContract.View>(), ShareContract.Presenter {

    override fun createModel(): ShareModel? = ShareModel()

    override fun getShareList(page: Int) {
        mModel?.getShareList(page)?.ss(mModel, mView, page == 1) {
            mView?.showShareList(it.data)
        }
    }

    override fun deleteShareArticle(id: Int) {
        mModel?.deleteShareArticle(id)?.ss(mModel, mView, true) {
            mView?.showDeleteArticle(true)
        }
    }

}