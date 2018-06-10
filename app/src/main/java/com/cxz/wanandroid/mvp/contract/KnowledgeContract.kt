package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * Created by chenxz on 2018/5/12.
 */
interface KnowledgeContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setKnowledgeList(articles: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestKnowledgeList(page: Int, cid: Int)

    }

}