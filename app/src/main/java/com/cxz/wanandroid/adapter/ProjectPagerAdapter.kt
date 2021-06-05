package com.cxz.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.text.Html
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cxz.wanandroid.mvp.model.bean.ProjectTreeBean
import com.cxz.wanandroid.ui.fragment.ProjectListFragment

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectPagerAdapter(private val list: MutableList<ProjectTreeBean>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(ProjectListFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = Html.fromHtml(list[position].name)

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE
}