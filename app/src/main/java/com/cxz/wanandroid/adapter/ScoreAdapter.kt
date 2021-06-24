package com.cxz.wanandroid.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cxz.wanandroid.R
import com.cxz.wanandroid.mvp.model.bean.UserScoreBean

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
class ScoreAdapter : BaseQuickAdapter<UserScoreBean, BaseViewHolder>(R.layout.item_socre_list), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: UserScoreBean) {
        holder.setText(R.id.tv_reason, item.reason)
            .setText(R.id.tv_desc, item.desc)
            .setText(R.id.tv_score, "+${item.coinCount}")
    }
}