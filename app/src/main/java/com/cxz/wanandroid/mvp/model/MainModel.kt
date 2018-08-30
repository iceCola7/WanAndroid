package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2018/8/30
 * @desc
 */
class MainModel {

    fun logout(): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.logout()
                .compose(SchedulerUtils.ioToMain())
    }

}