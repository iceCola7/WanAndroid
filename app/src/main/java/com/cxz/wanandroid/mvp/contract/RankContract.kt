package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView
import com.cxz.wanandroid.mvp.model.bean.BaseListResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.CoinInfoBean
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
interface RankContract {

    interface View : IView {
        fun showRankList(body: BaseListResponseBody<CoinInfoBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getRankList(page: Int)
    }

    interface Model : IModel {
        fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>>
    }

}