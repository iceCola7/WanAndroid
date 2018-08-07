package com.cxz.wanandroid.ui.activity

import android.support.design.widget.TabLayout
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.TodoPagerAdapter
import com.cxz.wanandroid.base.BaseSwipeBackActivity
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.mvp.model.bean.TodoTypeBean
import com.cxz.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.activity_todo.*

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

    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                val color = SettingUtil.getColor()
                tabLayout.setBackgroundColor(color)

                fab_menu.menuButtonColorNormal = color
                fab_menu.menuButtonColorPressed = color
                fab_menu.menuButtonColorRipple = color

                fab_add.colorNormal = color
                fab_add.colorPressed = color
                fab_add.colorRipple = color

                fab_todo.colorNormal = color
                fab_todo.colorPressed = color
                fab_todo.colorRipple = color

                fab_done.colorNormal = color
                fab_done.colorPressed = color
                fab_done.colorRipple = color

            }
        }
    }

}
