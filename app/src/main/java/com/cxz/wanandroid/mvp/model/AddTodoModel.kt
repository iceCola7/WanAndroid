package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.base.BaseModel
import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoModel : BaseModel(), AddTodoContract.Model {

    override fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.addTodo(map)
    }

    override fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodo(id, map)
    }

}