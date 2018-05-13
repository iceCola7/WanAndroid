package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * Created by chenxz on 2018/5/12.
 */
interface KnowledgeContract {

    interface View : IView {

        fun setKnowledgeList(articles: ArticleResponseBody)

    }

    interface Presenter : IPresenter<View> {

        fun requestKnowledgeList(page: Int, cid: Int)

    }

}