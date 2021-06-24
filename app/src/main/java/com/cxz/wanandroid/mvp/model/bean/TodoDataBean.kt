package com.cxz.wanandroid.mvp.model.bean

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * Created by chenxz on 2018/8/11.
 */
class TodoDataBean(var headerName: String = "", var todoBean: TodoBean? = null) : SectionEntity {

    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

}