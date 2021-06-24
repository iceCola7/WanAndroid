package com.cxz.wanandroid.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody

/**
 * Created by chenxz on 2018/5/9.
 */
class KnowledgeTreeAdapter : BaseQuickAdapter<KnowledgeTreeBody, BaseViewHolder>(R.layout.item_knowledge_tree_list),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: KnowledgeTreeBody) {
        holder.setText(R.id.title_first, item.name)
        item.children.let {
            holder.setText(
                R.id.title_second,
                it.joinToString("    ", transform = { child ->
                    Html.fromHtml(child.name)
                })
            )
        }
    }
}