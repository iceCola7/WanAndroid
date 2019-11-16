package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.SquareContract
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/11/16
 * @desc
 */
class SquareModel : CommonModel(), SquareContract.Model {
    override fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getSquareList(page)
    }
}