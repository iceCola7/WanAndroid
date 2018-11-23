package com.cxz.wanandroid.mvp.contract

/**
 * Created by chenxz on 2018/6/10.
 */
interface ContentContract {

    interface View : CommonContract.View {

    }

    interface Presenter : CommonContract.Presenter<View> {

    }

    interface Model : CommonContract.Model {

    }

}