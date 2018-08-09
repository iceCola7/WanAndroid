package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.TodoResponseBody

/**
 * Created by chenxz on 2018/8/7.
 */
interface TodoContract {

    interface View : IView {

        fun showNoTodoList(todoResponseBody: TodoResponseBody)

    }

    interface Presenter : IPresenter<View> {

        fun getAllTodoList(type: Int)

        fun getNoTodoList(page: Int, type: Int)

        fun getDoneList(page: Int, type: Int)

    }

}