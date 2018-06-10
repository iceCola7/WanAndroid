package com.cxz.wanandroid.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.Article

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectAdapter(private val context: Context?, datas: MutableList<Article>)
    : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_project_list, datas) {

    override fun convert(helper: BaseViewHolder?, item: Article?) {

        helper ?: return
        item ?: return
        helper.setText(R.id.item_project_list_title_tv, item.title)
                .setText(R.id.item_project_list_content_tv, item.desc)
                .setText(R.id.item_project_list_time_tv, item.niceDate)
                .setText(R.id.item_project_list_author_tv, item.author)
        context.let {
            Glide.with(it)
                    .load(item.envelopePic)
                    .into(helper.getView(R.id.item_project_list_iv))
        }

    }

}