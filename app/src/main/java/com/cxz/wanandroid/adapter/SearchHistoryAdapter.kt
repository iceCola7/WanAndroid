package com.cxz.wanandroid.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.SearchHistoryBean

class SearchHistoryAdapter(private val context: Context?, datas: MutableList<SearchHistoryBean>)
    : BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history, datas) {

    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper ?: return
        item ?: return

        helper.setText(R.id.tv_search_key, item.key)
                .addOnClickListener(R.id.iv_clear)

    }
}