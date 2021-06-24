package com.cxz.wanandroid.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.SearchHistoryBean

class SearchHistoryAdapter : BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history) {

    init {
        addChildClickViewIds(R.id.iv_clear)
    }

    override fun convert(holder: BaseViewHolder, item: SearchHistoryBean) {
        holder.setText(R.id.tv_search_key, item.key)
    }
}