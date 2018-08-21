package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.model.TodoModel

/**
 * Created by chenxz on 2018/8/7.
 */
class TodoPresenter : BasePresenter<TodoContract.View>(), TodoContract.Presenter {

    private val todoModel by lazy {
        TodoModel()
    }

    override fun getAllTodoList(type: Int) {
        val disposable = todoModel.getTodoList(type)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
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

    override fun getNoTodoList(page: Int, type: Int) {
        if (page == 1)
            mRootView?.showLoading()
        val disposable = todoModel.getNoTodoList(page, type)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showNoTodoList(results.data)
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

    override fun getDoneList(page: Int, type: Int) {
        if (page == 1)
            mRootView?.showLoading()
        val disposable = todoModel.getDoneList(page, type)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showNoTodoList(results.data)
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

    override fun deleteTodoById(id: Int) {
        val disposable = todoModel.deleteTodoById(id)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showDeleteSuccess(true)
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

    override fun updateTodoById(id: Int, status: Int) {
        val disposable = todoModel.updateTodoById(id, status)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mRootView?.apply {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        } else {
                            showUpdateSuccess(true)
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