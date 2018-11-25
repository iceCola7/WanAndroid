package com.cxz.wanandroid.ui.activity

import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.PopupWindow
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.TodoPopupAdapter
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.BaseSwipeBackActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.event.TodoEvent
import com.cxz.wanandroid.event.TodoTypeEvent
import com.cxz.wanandroid.mvp.model.bean.TodoTypeBean
import com.cxz.wanandroid.ui.fragment.TodoFragment
import com.cxz.wanandroid.utils.DisplayManager
import com.cxz.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.activity_todo.*
import org.greenrobot.eventbus.EventBus

class TodoActivity : BaseSwipeBackActivity() {

    private var mType = 0

    private var mTodoFragment: TodoFragment? = null

    private lateinit var datas: MutableList<TodoTypeBean>
    /**
     * PopupWindow
     */
    private var mSwitchPopupWindow: PopupWindow? = null

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initData() {
        datas = getData()
    }

    override fun initView() {
        toolbar.run {
            title = datas[0].name // getString(R.string.nav_todo)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (mTodoFragment == null) {
            mTodoFragment = TodoFragment.getInstance(mType)
            transaction.add(R.id.container, mTodoFragment!!, "todo")
        } else {
            transaction.show(mTodoFragment!!)
        }
        transaction.commit()

        fab_menu.setClosedOnTouchOutside(true)
        fab_add.setOnClickListener(clickListener)
        fab_todo.setOnClickListener(clickListener)
        fab_done.setOnClickListener(clickListener)

    }

    private val clickListener = View.OnClickListener {
        fab_menu.close(true)
        when (it.id) {
            R.id.fab_add -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD, mType))
            }
            R.id.fab_todo -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_NO, mType))
            }
            R.id.fab_done -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE, mType))
            }
        }
    }

    override fun start() {
    }

    private fun getData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个", true))
        list.add(TodoTypeBean(1, "工作", false))
        list.add(TodoTypeBean(2, "学习", false))
        list.add(TodoTypeBean(3, "生活", false))
        return list
    }

    private fun initPopupWindow(dataList: List<TodoTypeBean>) {
        val recyclerView = layoutInflater.inflate(R.layout.layout_popup_todo, null) as RecyclerView
        val adapter = TodoPopupAdapter()
        adapter.setNewData(dataList)
        adapter.setOnItemClickListener { adapter, view, position ->
            mSwitchPopupWindow?.dismiss()
            val itemData = adapter.data[position] as TodoTypeBean
            mType = itemData.type
            toolbar.title = itemData.name
            adapter.data.forEachIndexed { index, any ->
                val item = any as TodoTypeBean
                item.isSelected = index == position
            }
            adapter.notifyDataSetChanged()
            EventBus.getDefault().post(TodoTypeEvent(mType))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(App.context)
            this.adapter = adapter
        }
        mSwitchPopupWindow = PopupWindow(recyclerView)
        mSwitchPopupWindow?.apply {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = DisplayManager.dip2px(10F).toFloat()
            }
            // setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
    }

    private fun showPopupWindow(dataList: MutableList<TodoTypeBean>) {
        if (mSwitchPopupWindow == null) initPopupWindow(dataList)
        if (mSwitchPopupWindow?.isShowing == true) mSwitchPopupWindow?.dismiss()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSwitchPopupWindow?.showAsDropDown(toolbar, -DisplayManager.dip2px(5F), 0, Gravity.END)
        } else {
            mSwitchPopupWindow?.showAtLocation(toolbar, Gravity.BOTTOM, -DisplayManager.dip2px(5F), 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_todo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_todo_type -> {
                showPopupWindow(datas)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    private fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                val color = SettingUtil.getColor()
                // tabLayout.setBackgroundColor(color)

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
