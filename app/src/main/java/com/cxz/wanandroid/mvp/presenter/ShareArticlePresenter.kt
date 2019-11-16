package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.ShareArticleContract
import com.cxz.wanandroid.mvp.model.ShareArticleModel

/**
 * @author chenxz
 * @date 2019/11/16
 * @desc
 */
class ShareArticlePresenter : BasePresenter<ShareArticleModel, ShareArticleContract.View>(), ShareArticleContract.Presenter {

    override fun createModel(): ShareArticleModel? = ShareArticleModel()

    override fun shareArticle() {
        val title = mView?.getArticleTitle().toString()
        val link = mView?.getArticleLink().toString()

        if (title.isEmpty()) {
            mView?.showMsg("文章标题不能为空")
            return
        }
        if (link.isEmpty()) {
            mView?.showMsg("文章链接不能为空")
            return
        }
        val map = mutableMapOf<String, Any>()
        map["title"] = title
        map["link"] = link
        mModel?.shareArticle(map)?.ss(mModel, mView, true) {
            mView?.showShareArticle(true)
        }

    }

}