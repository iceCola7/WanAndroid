package com.cxz.wanandroid.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * BaseQuickAdapter 扩展
 */

/**
 * 刷新全部数据 或者 追加数据
 *
 * @param isRefreshAllData Boolean  是否刷新所有数据
 * @param expectedDataSize 预期数据量，用于处理加载更多（如：预期10条数据，小于10条则关闭加载更多）
 * @param data List<T>  数据集合
 */
fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setNewOrAddData(
    isRefreshAllData: Boolean,
    expectedDataSize: Int,
    data: MutableList<T>?,
) {
    if (data == null) {
        return
    }
    if (isRefreshAllData) {
        setNewInstance(data)
        loadMoreModule.checkDisableLoadMoreIfNotFullPage()
    } else {
        addData(data)
        val isEnableLoadMore = loadMoreModule.isEnableLoadMore
        if (!isEnableLoadMore) {
            return
        }
    }
    // 处理加载更多    End/Complete（End：不会再触发上拉加载更多，Complete：还会继续触发上拉加载更多）
    if (data.size < expectedDataSize) {
        // 加载更多结束（true：不展示加载更多结束的view，false则展示）
        loadMoreModule.loadMoreEnd()
    } else {
        loadMoreModule.loadMoreComplete()
    }
}

/**
 * 刷新全部数据 或者 追加数据
 *
 * @param isSetNewData Boolean  是否设置新数据，全刷新，否则追加数据，局部刷新
 * @param data List<T>  数据集合
 */
fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setNewOrAddData(isSetNewData: Boolean, data: MutableList<T>?) {
    if (isSetNewData) {
        setNewInstance(data)
        loadMoreModule.checkDisableLoadMoreIfNotFullPage()
    } else {
        if (data != null) {
            addData(data)
        }
        val isEnableLoadMore = loadMoreModule.isEnableLoadMore
        if (!isEnableLoadMore) {
            return
        }
    }
    // 处理加载更多    End/Complete（End：不会再触发上拉加载更多，Complete：还会继续触发上拉加载更多）
    if (data == null || data.isEmpty()) {
        // 加载更多结束（true：不展示「加载更多结束」的view，false则展示）
        loadMoreModule.loadMoreEnd(false)
    } else {
        loadMoreModule.loadMoreComplete()
    }
}
