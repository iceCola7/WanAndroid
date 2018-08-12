package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.AddTodoBean
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.UpdateTodoBean
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoModel {

    fun addTodo(body: AddTodoBean): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.addTodo(body)
                .compose(SchedulerUtils.ioToMain())
    }

    fun updateTodo(id: Int, body: UpdateTodoBean): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodo(id, body)
                .compose(SchedulerUtils.ioToMain())
    }

}