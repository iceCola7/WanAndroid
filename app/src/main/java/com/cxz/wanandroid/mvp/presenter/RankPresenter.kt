package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.RankContract
import com.cxz.wanandroid.mvp.model.RankModel

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
class RankPresenter : BasePresenter<RankContract.Model, RankContract.View>(), RankContract.Presenter {

    override fun createModel(): RankContract.Model? = RankModel()

    override fun getRankList(page: Int) {
        mModel?.getRankList(page)?.ss(mModel, mView) {
            mView?.showRankList(it.data)
        }
    }
}