package com.cxz.wanandroid.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.cxz.multiplestatusview.MultipleStatusView
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.utils.StatusBarUtil

/**
 * Created by chenxz on 2018/4/21.
 */
abstract class BaseActivity : AppCompatActivity() {

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
        StatusBarUtil.darkMode(this, Color.TRANSPARENT,0F)
        initData()
        initView()
        start()
        initListener()
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

    /**
     * 打卡软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        App.getRefWatcher(this)?.watch(this)
    }

}