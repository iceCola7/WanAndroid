package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.base.BaseModel
import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.RankContract
import com.cxz.wanandroid.mvp.model.bean.BaseListResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.CoinInfoBean
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
class RankModel : BaseModel(), RankContract.Model {
    override fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>> {
        return RetrofitHelper.service.getRankList(page)
    }
}