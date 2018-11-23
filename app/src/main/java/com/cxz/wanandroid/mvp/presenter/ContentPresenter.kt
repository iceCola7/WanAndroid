package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.mvp.contract.ContentContract
import com.cxz.wanandroid.mvp.model.ContentModel

/**
 * Created by chenxz on 2018/6/10.
 */
class ContentPresenter : CommonPresenter<ContentContract.Model, ContentContract.View>(), ContentContract.Presenter {

    override fun createModel(): ContentContract.Model? = ContentModel()

}