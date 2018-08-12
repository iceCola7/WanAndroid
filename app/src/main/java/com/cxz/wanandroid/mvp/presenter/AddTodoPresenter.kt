package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.AddTodoModel
import com.cxz.wanandroid.mvp.model.bean.AddTodoBean
import com.cxz.wanandroid.mvp.model.bean.UpdateTodoBean

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoPresenter : BasePresenter<AddTodoContract.View>(), AddTodoContract.Presenter {

    private val addTodoModel: AddTodoModel by lazy {
        AddTodoModel()
    }

    override fun addTodo(body: AddTodoBean) {
        mRootView?.showLoading()
        val disposable = addTodoModel.addTodo(body)
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

    override fun updateTodo(id: Int, body: UpdateTodoBean) {
        mRootView?.showLoading()
        val disposable = addTodoModel.updateTodo(id, body)
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