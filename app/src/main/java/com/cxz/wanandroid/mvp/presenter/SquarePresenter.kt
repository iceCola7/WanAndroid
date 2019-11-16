package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.SquareContract
import com.cxz.wanandroid.mvp.model.SquareModel

/**
 * @author chenxz
 * @date 2019/11/16
 * @desc
 */
class SquarePresenter : CommonPresenter<SquareModel, SquareContract.View>(), SquareContract.Presenter {

    override fun createModel(): SquareModel? = SquareModel()

    override fun getSquareList(page: Int) {
        mModel?.getSquareList(page)?.ss(mModel, mView, page == 0) {
            mView?.showSquareList(it.data)
        }
    }

}