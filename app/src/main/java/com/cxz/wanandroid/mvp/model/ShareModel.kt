package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.ShareContract
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.ShareResponseBody
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
class ShareModel : CommonModel(), ShareContract.Model {
    override fun getShareList(page: Int): Observable<HttpResult<ShareResponseBody>> {
        return RetrofitHelper.service.getShareList(page)
    }

    override fun deleteShareArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteShareArticle(id)
    }
}