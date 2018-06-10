package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.mvp.contract.ContentContract
import com.cxz.wanandroid.mvp.model.ContentModel

/**
 * Created by chenxz on 2018/6/10.
 */
class ContentPresenter : CommonPresenter<ContentContract.View>(), ContentContract.Presenter {

    private val contentModel: ContentModel by lazy {
        ContentModel()
    }

}