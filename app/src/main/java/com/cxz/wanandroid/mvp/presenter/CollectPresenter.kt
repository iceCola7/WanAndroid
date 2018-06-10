package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.mvp.contract.CollectContract
import com.cxz.wanandroid.mvp.model.CollectModel

/**
 * Created by chenxz on 2018/6/9.
 */
class CollectPresenter : BasePresenter<CollectContract.View>(), CollectContract.Presenter {

    private val collectModel by lazy {
        CollectModel()
    }


    override fun getCollectList(page: Int) {
        mRootView?.showLoading()
        val disposable = collectModel.getCollectList(page)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            setCollectList(results.data)
                        }
                        hideLoading()
                    }
                })
        addSubscription(disposable)
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        val disposable = collectModel.removeCollectArticle(id, originId)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showRemoveCollectSuccess(true)
                        }
                        hideLoading()
                    }
                })
        addSubscription(disposable)
    }

}