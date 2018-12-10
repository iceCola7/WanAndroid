package com.cxz.wanandroid.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.TodoTypeBean

/**
 * @author chenxz
 * @date 2018/11/25
 * @desc
 */
class TodoPopupAdapter : BaseQuickAdapter<TodoTypeBean, BaseViewHolder>(R.layout.item_todo_popup_list) {

    override fun convert(helper: BaseViewHolder?, item: TodoTypeBean?) {
        helper ?: return
        item ?: return
        val tv_popup = helper.getView<TextView>(R.id.tv_popup)
        tv_popup.text = item.name
        if (item.isSelected) {
            tv_popup.setTextColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            tv_popup.setTextColor(mContext.resources.getColor(R.color.common_color))
        }
    }
}