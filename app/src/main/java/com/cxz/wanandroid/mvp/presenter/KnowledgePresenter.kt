package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.mvp.contract.KnowledgeContract
import com.cxz.wanandroid.mvp.model.KnowledgeModel

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgePresenter : BasePresenter<KnowledgeContract.View>(), KnowledgeContract.Presenter {

    private val knowledgeModel by lazy {
        KnowledgeModel()
    }

    override fun requestKnowledgeTree() {
        mRootView?.showLoading()
        val disposable = knowledgeModel.requestKnowledgeTree()
                .subscribe({ results ->
                    mRootView?.apply {
                        hideLoading()
                        setKnowledgeTree(results.data)
                    }
                })
        addSubscription(disposable)
    }

}