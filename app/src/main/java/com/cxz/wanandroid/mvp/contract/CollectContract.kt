package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.CollectionArticle
import com.cxz.wanandroid.mvp.model.bean.CollectionResponseBody

/**
 * Created by chenxz on 2018/6/9.
 */
interface CollectContract {

    interface View : IView {

        fun setCollectList(articles: CollectionResponseBody<CollectionArticle>)

        fun showRemoveCollectSuccess(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun getCollectList(page: Int)

        fun removeCollectArticle(id: Int, originId: Int)

    }

}