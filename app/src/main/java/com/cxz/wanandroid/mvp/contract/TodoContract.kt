package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView

/**
 * Created by chenxz on 2018/8/7.
 */
interface TodoContract {

    interface View : IView {

    }

    interface Presenter : IPresenter<View> {

        fun getAllTodoList(type: Int)

        fun getTodoList(type: Int, page: Int)

    }

}