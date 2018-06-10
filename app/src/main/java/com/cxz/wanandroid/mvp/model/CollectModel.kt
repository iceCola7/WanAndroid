package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.CollectionArticle
import com.cxz.wanandroid.mvp.model.bean.CollectionResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/6/9.
 */
class CollectModel {

    fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>> {
        return RetrofitHelper.service.getCollectList(page)
                .compose(SchedulerUtils.ioToMain())
    }

    fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.removeCollectArticle(id, originId)
                .compose(SchedulerUtils.ioToMain())
    }

}