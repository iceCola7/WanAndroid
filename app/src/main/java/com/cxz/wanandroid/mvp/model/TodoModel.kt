package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.base.BaseModel
import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.model.bean.AllTodoResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.TodoResponseBody
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/8/7.
 */
class TodoModel : BaseModel(), TodoContract.Model {

    override fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>> {
        return RetrofitHelper.service.getTodoList(type)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getNoTodoList(page, type)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getDoneList(page, type)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun deleteTodoById(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteTodoById(id)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodoById(id, status)
                .compose(SchedulerUtils.ioToMain())
    }

}