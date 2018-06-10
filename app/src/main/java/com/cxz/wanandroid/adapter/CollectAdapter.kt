package com.cxz.wanandroid.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.CollectionArticle

/**
 * Created by chenxz on 2018/6/10.
 */
class CollectAdapter(private val context: Context?, datas: MutableList<CollectionArticle>)
    : BaseQuickAdapter<CollectionArticle, BaseViewHolder>(R.layout.item_collect_list, datas) {
    override fun convert(helper: BaseViewHolder?, item: CollectionArticle?) {

        helper ?: return
        item ?: return
        helper.setText(R.id.tv_article_title, item.title)
                .setText(R.id.tv_article_author, item.author)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_like, R.drawable.ic_like)
                .addOnClickListener(R.id.iv_like)

        if (item.chapterName.isNotEmpty()) {
            helper.setText(R.id.tv_article_chapterName, item.chapterName)
            helper.getView<TextView>(R.id.tv_article_chapterName).visibility = View.VISIBLE
        } else {
            helper.getView<TextView>(R.id.tv_article_chapterName).visibility = View.INVISIBLE
        }
        if (item.envelopePic.isNotEmpty()) {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.VISIBLE
            context?.let {
                Glide.with(it)
                        .load(item.envelopePic)
                        .into(helper.getView(R.id.iv_article_thumbnail))
            }
        } else {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.GONE
        }
    }
}