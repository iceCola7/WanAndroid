package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.model.TodoModel

/**
 * Created by chenxz on 2018/8/7.
 */
class TodoPresenter : BasePresenter<TodoContract.Model, TodoContract.View>(), TodoContract.Presenter {

    override fun createModel(): TodoContract.Model? = TodoModel()

    override fun getAllTodoList(type: Int) {
        val disposable = mModel?.getTodoList(type)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
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

    override fun getNoTodoList(page: Int, type: Int) {
        if (page == 1)
            mView?.showLoading()
        val disposable = mModel?.getNoTodoList(page, type)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showNoTodoList(results.data)
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

    override fun getDoneList(page: Int, type: Int) {
        if (page == 1)
            mView?.showLoading()
        val disposable = mModel?.getDoneList(page, type)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showNoTodoList(results.data)
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

    override fun deleteTodoById(id: Int) {
        val disposable = mModel?.deleteTodoById(id)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showDeleteSuccess(true)
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

    override fun updateTodoById(id: Int, status: Int) {
        val disposable = mModel?.updateTodoById(id, status)
                ?.retryWhen(RetryWithDelay())
                ?.subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showUpdateSuccess(true)
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