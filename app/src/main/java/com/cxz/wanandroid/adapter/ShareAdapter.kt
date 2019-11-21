package com.cxz.wanandroid.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.utils.ImageLoader

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
class ShareAdapter(datas: MutableList<Article>)
    : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_share_list, datas) {

    override fun convert(helper: BaseViewHolder?, item: Article?) {
        item ?: return
        helper ?: return
        val authorStr = if (item.author.isNotEmpty()) item.author else item.shareUser
        helper.setText(R.id.tv_article_title, Html.fromHtml(item.title))
                .setText(R.id.tv_article_author, authorStr)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_like, if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not)
                .addOnClickListener(R.id.iv_like)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.rl_content)
        val chapterName = when {
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() ->
                "${item.superChapterName} / ${item.chapterName}"
            item.superChapterName.isNotEmpty() -> item.superChapterName
            item.chapterName.isNotEmpty() -> item.chapterName
            else -> ""
        }
        helper.setText(R.id.tv_article_chapterName, chapterName)
        if (item.envelopePic.isNotEmpty()) {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.VISIBLE
            mContext?.let {
                ImageLoader.load(it, item.envelopePic, helper.getView(R.id.iv_article_thumbnail))
            }
        } else {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.GONE
        }
        val tv_fresh = helper.getView<TextView>(R.id.tv_article_fresh)
        if (item.fresh) {
            tv_fresh.visibility = View.VISIBLE
        } else {
            tv_fresh.visibility = View.GONE
        }
        val tv_top = helper.getView<TextView>(R.id.tv_article_top)
        if (item.top == "1") {
            tv_top.visibility = View.VISIBLE
        } else {
            tv_top.visibility = View.GONE
        }
        val tv_article_tag = helper.getView<TextView>(R.id.tv_article_tag)
        if (item.tags.size > 0) {
            tv_article_tag.visibility = View.VISIBLE
            tv_article_tag.text = item.tags[0].name
        } else {
            tv_article_tag.visibility = View.GONE
        }
        val tv_article_audit = helper.getView<TextView>(R.id.tv_article_audit)
        if (item.audit == 0) {
            tv_article_audit.visibility = View.VISIBLE
            tv_article_audit.text = mContext.getString(R.string.audited)
        } else {
            tv_article_audit.visibility = View.GONE
        }

    }
}