package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.AddTodoModel

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoPresenter : BasePresenter<AddTodoContract.View>(), AddTodoContract.Presenter {

    private val addTodoModel: AddTodoModel by lazy {
        AddTodoModel()
    }

    override fun addTodo() {
        val type = mRootView?.getType() ?: 0
        val title = mRootView?.getTitle().toString()
        val content = mRootView?.getContent().toString()
        val date = mRootView?.getCurrentDate().toString()
        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date

        mRootView?.showLoading()
        val disposable = addTodoModel.addTodo(map)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showAddTodo(true)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }

    override fun updateTodo(id: Int) {
        val type = mRootView?.getType() ?: 0
        val title = mRootView?.getTitle().toString()
        val content = mRootView?.getContent().toString()
        val date = mRootView?.getCurrentDate().toString()
        val status = mRootView?.getStatus() ?: 0
        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["status"] = status
        mRootView?.showLoading()
        val disposable = addTodoModel.updateTodo(id, map)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showUpdateTodo(true)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }


}