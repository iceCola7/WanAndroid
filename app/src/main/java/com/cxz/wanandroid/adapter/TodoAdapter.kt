package com.cxz.wanandroid.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.TodoBean
import com.cxz.wanandroid.mvp.model.bean.TodoDataBean
import com.cxz.wanandroid.widget.TiltTextView

/**
 * Created by chenxz on 2018/8/8.
 */
class TodoAdapter : BaseSectionQuickAdapter<TodoDataBean, BaseViewHolder> {

    constructor(layoutResId: Int, sectionHeadResId: Int, data: MutableList<TodoDataBean>) : super(layoutResId, sectionHeadResId, data)

    override fun convertHead(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.tv_header, item.header)
    }

    override fun convert(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item ?: return
        val itemData = item.t as TodoBean
        helper.setText(R.id.tv_todo_title, itemData.title)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.btn_done)
                .addOnClickListener(R.id.item_todo_content)
        val tv_todo_desc = helper.getView<TextView>(R.id.tv_todo_desc)
        tv_todo_desc.text = ""
        tv_todo_desc.visibility = View.INVISIBLE
        if (itemData.content.isNotEmpty()) {
            tv_todo_desc.visibility = View.VISIBLE
            tv_todo_desc.text = itemData.content
        }
        val btn_done = helper.getView<Button>(R.id.btn_done)
        if (itemData.status == 0) {
            btn_done.text = mContext.resources.getString(R.string.mark_done)
        } else if (itemData.status == 1) {
            btn_done.text = mContext.resources.getString(R.string.restore)
        }
        val tv_tilt = helper.getView<TiltTextView>(R.id.tv_tilt)
        if (itemData.priority == 1) {
            tv_tilt.setText(mContext.resources.getString(R.string.priority_1))
            tv_tilt.visibility = View.VISIBLE
        } else {
            tv_tilt.visibility = View.GONE
        }
    }


}