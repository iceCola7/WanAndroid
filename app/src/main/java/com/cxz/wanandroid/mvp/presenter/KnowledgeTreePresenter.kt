package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.KnowledgeTreeContract
import com.cxz.wanandroid.mvp.model.KnowledgeTreeModel

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.Model, KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {

    override fun createModel(): KnowledgeTreeContract.Model? = KnowledgeTreeModel()

    override fun requestKnowledgeTree() {
        mView?.showLoading()
        val disposable = mModel?.requestKnowledgeTree()
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setKnowledgeTree(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}