package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.base.BaseModel
import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.ShareArticleContract
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
class ShareArticleModel : BaseModel(), ShareArticleContract.Model {
    override fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.shareArticle(map)
    }
}