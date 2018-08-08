package com.cxz.wanandroid.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.TodoBean

/**
 * Created by chenxz on 2018/8/8.
 */
class TodoAdapter(datas: MutableList<TodoBean>)
    : BaseQuickAdapter<TodoBean, BaseViewHolder>(R.layout.item_todo_list, datas) {

    override fun convert(helper: BaseViewHolder?, item: TodoBean?) {

        helper ?: return
        item ?: return

        helper.setText(R.id.tv_todo_title, item.title)
        val tv_todo_desc = helper.getView<TextView>(R.id.tv_todo_desc)
        tv_todo_desc.visibility = View.INVISIBLE
        if (item.content.isNotEmpty()) {
            tv_todo_desc.visibility = View.VISIBLE
            tv_todo_desc.text = item.content
        }
    }
}