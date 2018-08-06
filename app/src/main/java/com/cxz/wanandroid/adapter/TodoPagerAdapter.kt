package com.cxz.wanandroid.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.cxz.wanandroid.mvp.model.bean.TodoTypeBean
import com.cxz.wanandroid.ui.fragment.TodoFragment

/**
 * Created by chenxz on 2018/8/6.
 */
class TodoPagerAdapter(val list: List<TodoTypeBean>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = mutableListOf<TodoFragment>()

    init {
        list.forEach {
            fragments.add(TodoFragment.getInstance(it.type))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

}