package com.cxz.wanandroid.ui.activity

import android.support.design.widget.TabLayout
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.TodoPagerAdapter
import com.cxz.wanandroid.base.BaseSwipeBackActivity
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.mvp.model.bean.TodoTypeBean
import com.cxz.wanandroid.utils.SettingUtil
import com.cxz.wanandroid.widget.TabLayoutHelper
import kotlinx.android.synthetic.main.activity_knowledge.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TodoActivity : BaseSwipeBackActivity() {

    /**
     * ViewPagerAdapter
     */
    private lateinit var viewPagerAdapter: TodoPagerAdapter

    private lateinit var datas: MutableList<TodoTypeBean>

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initData() {
        datas = getData()
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.nav_todo)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewPagerAdapter = TodoPagerAdapter(datas, supportFragmentManager)
        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = datas.size
        }
        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }
    }

    override fun start() {
    }

    private fun getData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个"))
        list.add(TodoTypeBean(1, "工作"))
        list.add(TodoTypeBean(2, "学习"))
        list.add(TodoTypeBean(3, "生活"))
        return list
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

}
