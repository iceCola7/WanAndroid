package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/5/8.
 */
interface KnowledgeTreeContract {

    interface View : IView {

        fun scrollToTop()

        fun setKnowledgeTree(lists: List<KnowledgeTreeBody>)

    }

    interface Presenter : IPresenter<View> {

        fun requestKnowledgeTree()

    }

    interface Model : IModel {

        fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    }

}