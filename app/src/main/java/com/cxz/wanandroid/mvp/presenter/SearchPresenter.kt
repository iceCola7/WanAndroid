package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.SearchContract
import com.cxz.wanandroid.mvp.model.SearchModel
import com.cxz.wanandroid.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val searchModel by lazy {
        SearchModel()
    }

    override fun deleteById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }

    }

    override fun clearAllHistory() {
        doAsync {
            val count = LitePal.deleteAll(SearchHistoryBean::class.java)
            loge("------------>>$count")
            uiThread {

            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key)
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            if (!historyBeans.contains(historyBean))
                historyBean.save()
        }
    }

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            uiThread {
                mRootView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun getHotSearchData() {
        mRootView?.showLoading()
        val disposable = searchModel.getHotSearchData()
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showHotSearchData(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}