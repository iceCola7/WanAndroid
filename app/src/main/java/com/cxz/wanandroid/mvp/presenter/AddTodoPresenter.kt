package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.AddTodoModel

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoPresenter : BasePresenter<AddTodoContract.Model, AddTodoContract.View>(), AddTodoContract.Presenter {

    override fun createModel(): AddTodoContract.Model? = AddTodoModel()

    override fun addTodo() {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date

        mView?.showLoading()
        val disposable = mModel?.addTodo(map)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showAddTodo(true)
                        }
                        hideLoading()
                    }
                }, {
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }

    override fun updateTodo(id: Int) {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val status = mView?.getStatus() ?: 0
        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["status"] = status
        mView?.showLoading()
        val disposable = mModel?.updateTodo(id, map)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showUpdateTodo(true)
                        }
                        hideLoading()
                    }
                }, {
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }


}