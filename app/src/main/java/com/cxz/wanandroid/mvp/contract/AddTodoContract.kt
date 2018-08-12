package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.AddTodoBean
import com.cxz.wanandroid.mvp.model.bean.UpdateTodoBean

/**
 * Created by chenxz on 2018/8/11.
 */
interface AddTodoContract {

    interface View : IView {

        fun showAddTodo(success: Boolean)

        fun showUpdateTodo(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun addTodo(body: AddTodoBean)

        fun updateTodo(id: Int, body: UpdateTodoBean)

    }

}