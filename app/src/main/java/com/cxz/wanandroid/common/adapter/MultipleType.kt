package com.cxz.wanandroid.common.adapter

/**
 * desc: 多布局条目类型
 */
@Deprecated("")
interface MultipleType<in T> {
    fun getLayoutId(item: T, position: Int): Int
}
