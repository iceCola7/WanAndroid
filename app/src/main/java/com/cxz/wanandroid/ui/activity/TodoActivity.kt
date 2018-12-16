package com.cxz.wanandroid.ui.activity

import android.content.res.ColorStateList
import android.os.Build
import android.support.design.widget.BottomNavigationView
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
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        datas = getTypeData()
    }

    override fun initView() {
        toolbar.run {
            title = datas[0].name // getString(R.string.nav_todo)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        floating_action_btn.run {
            setOnClickListener {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD, mType))
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (mTodoFragment == null) {
            mTodoFragment = TodoFragment.getInstance(mType)
            transaction.add(R.id.container, mTodoFragment!!, "todo")
        } else {
            transaction.show(mTodoFragment!!)
        }
        transaction.commit()
    }

    override fun start() {
    }

    private fun getTypeData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个", true))
        list.add(TodoTypeBean(1, "工作", false))
        list.add(TodoTypeBean(2, "学习", false))
        list.add(TodoTypeBean(3, "生活", false))
        return list
    }

    /**
     * 初始化 PopupWindow
     */
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
            bottom_navigation.selectedItemId = R.id.action_notodo
            EventBus.getDefault().post(TodoTypeEvent(mType))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodoActivity)
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
            setOnDismissListener {
                dismiss()
            }
            setTouchInterceptor { v, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    dismiss()
                    true
                }
                false
            }
        }
    }

    /**
     * 展示 PopupWindow
     */
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.action_notodo -> {
                        EventBus.getDefault().post(TodoEvent(Constant.TODO_NO, mType))
                        true
                    }
                    R.id.action_completed -> {
                        EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE, mType))
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

}
