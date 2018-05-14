package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.NavigationBean
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/5/13.
 */
class NavigationModel {

    fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> {
        return RetrofitHelper.service.getNavigationList()
                .compose(SchedulerUtils.ioToMain())
    }

}