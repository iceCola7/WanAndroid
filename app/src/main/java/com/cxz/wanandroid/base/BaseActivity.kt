package com.cxz.wanandroid.base

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.color.CircleView
import com.cxz.multiplestatusview.MultipleStatusView
import com.cxz.swipelibrary.SwipeBackActivity
import com.cxz.swipelibrary.SwipeBackLayout
import com.cxz.wanandroid.R
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.utils.CommonUtil
import com.cxz.wanandroid.utils.Preference
import com.cxz.wanandroid.utils.SettingUtil
import com.cxz.wanandroid.utils.StatusBarUtil

/**
 * Created by chenxz on 2018/4/21.
 */
abstract class BaseActivity : SwipeBackActivity() {

    /**
     * check login
     */
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    /**
     * 布局文件id
     */
    protected abstract fun attachLayoutRes(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        initSwipeBack()
        initData()
        initView()
        start()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (!SettingUtil.getIsNightMode()){
            initColor()
        }
    }

    open fun initColor() {
        val color = SettingUtil.getColor()
        if (supportActionBar != null)
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(color))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = CircleView.shiftColorDown(color)
            // 最近任务栏上色
            val tDesc = ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
                    color)
            setTaskDescription(tDesc)
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = CircleView.shiftColorDown(color)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }

    private fun initSwipeBack() {
        setSwipeBackEnable(enableSwipeBack())
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }

    /**
     * SwipeBack Enable
     */
    open fun enableSwipeBack(): Boolean {
        return true
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    protected fun initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean, title: String) {
        toolbar?.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonUtil.fixInputMethodManagerLeak(this)
        App.getRefWatcher(this)?.watch(this)
    }

}