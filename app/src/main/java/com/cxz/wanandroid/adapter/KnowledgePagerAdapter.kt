package com.cxz.wanandroid.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.cxz.wanandroid.mvp.model.bean.Knowledge
import com.cxz.wanandroid.ui.fragment.KnowledgeFragment

/**
 * Created by chenxz on 2018/5/10.
 */
class KnowledgePagerAdapter(val list: List<Knowledge>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        list.forEach {
            fragments.add(KnowledgeFragment.newInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}