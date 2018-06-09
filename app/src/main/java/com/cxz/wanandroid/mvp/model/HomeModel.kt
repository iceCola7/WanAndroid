package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by chenxz on 2018/4/21.
 */
class HomeModel {

    fun requestBanner(): Observable<HttpResult<List<Banner>>> {
        return RetrofitHelper.service.getBanners()
                .compose(SchedulerUtils.ioToMain())
    }

    fun requestArticles(num: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getArticles(num)
                .compose(SchedulerUtils.ioToMain())
    }

    fun addCollectArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.addCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun cancelCollectArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.cancelCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }

}