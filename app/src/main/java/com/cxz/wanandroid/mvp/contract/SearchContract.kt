package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView

interface SearchContract {

    interface View: IView{

    }

    interface Presenter: IPresenter<View>{

    }

}