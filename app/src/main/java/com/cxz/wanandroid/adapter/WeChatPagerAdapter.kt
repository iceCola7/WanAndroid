package com.cxz.wanandroid.adapter

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cxz.wanandroid.mvp.model.bean.WXChapterBean
import com.cxz.wanandroid.ui.fragment.KnowledgeFragment

/**
 * @author chenxz
 * @date 2018/10/28
 * @desc
 */
class WeChatPagerAdapter(private val list: MutableList<WXChapterBean>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = Html.fromHtml(list[position].name)

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE


}