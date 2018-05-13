package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.KnowledgeTreeContract
import com.cxz.wanandroid.mvp.model.KnowledgeTreeModel

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {

    private val knowledgeTreeModel by lazy {
        KnowledgeTreeModel()
    }

    override fun requestKnowledgeTree() {
        mRootView?.showLoading()
        val disposable = knowledgeTreeModel.requestKnowledgeTree()
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setKnowledgeTree(results.data)
                        }
                        hideLoading()
                    }
                })
        addSubscription(disposable)
    }

}