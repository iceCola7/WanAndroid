package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.AllTodoResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.TodoResponseBody
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/8/7.
 */
interface TodoContract {

    interface View : IView {

        fun showNoTodoList(todoResponseBody: TodoResponseBody)

        fun showDeleteSuccess(success: Boolean)

        fun showUpdateSuccess(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun getAllTodoList(type: Int)

        fun getNoTodoList(page: Int, type: Int)

        fun getDoneList(page: Int, type: Int)

        fun deleteTodoById(id: Int)

        fun updateTodoById(id: Int, status: Int)

    }

    interface Model : IModel {

        fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>>

        fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun deleteTodoById(id: Int): Observable<HttpResult<Any>>

        fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>>

    }

}