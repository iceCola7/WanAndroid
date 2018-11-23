package com.cxz.wanandroid.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseMvpActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.event.LoginEvent
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.MainContract
import com.cxz.wanandroid.mvp.presenter.MainPresenter
import com.cxz.wanandroid.ui.fragment.*
import com.cxz.wanandroid.ui.setting.SettingActivity
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.utils.Preference
import com.cxz.wanandroid.utils.SettingUtil
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    private val BOTTOM_INDEX: String = "bottom_index"

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE = 0x02
    private val FRAGMENT_NAVIGATION = 0x03
    private val FRAGMENT_PROJECT = 0x04
    private val FRAGMENT_WECHAT = 0x05

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment: HomeFragment? = null
    private var mKnowledgeTreeFragment: KnowledgeTreeFragment? = null
    private var mNavigationFragment: NavigationFragment? = null
    private var mProjectFragment: ProjectFragment? = null
    private var mWeChatFragment: WeChatFragment? = null

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    /**
     * local username
     */
    private val username: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * username TextView
     */
    private var nav_username: TextView? = null

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun initData() {
        Beta.checkUpgrade(false, false)
    }

    override fun useEventBus(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawerLayout()

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username?.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }
            setOnClickListener {
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                } else {

                }
            }
        }

        showFragment(mIndex)

        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    override fun start() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        if (event.isLogin) {
            nav_username?.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
        } else {
            nav_username?.text = resources.getString(R.string.login)
            nav_view.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent) {
        if (event.isRefresh) {
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    /**
     * init DrawerLayout
     */
    private fun initDrawerLayout() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            var params = window.attributes
//            params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//            drawer_layout.fitsSystemWindows = true
//            drawer_layout.clipToPadding = false
//        }
        drawer_layout.run {
            var toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    this,
                    toolbar
                    , R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BOTTOM_INDEX, mIndex)
    }

    /**
     * 展示Fragment
     * @param index
     */
    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index) {
            FRAGMENT_HOME // 首页
            -> {
                toolbar.title = getString(R.string.app_name)
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container, mHomeFragment!!, "home")
                } else {
                    transaction.show(mHomeFragment!!)
                }
            }
            FRAGMENT_KNOWLEDGE // 知识体系
            -> {
                toolbar.title = getString(R.string.knowledge_system)
                if (mKnowledgeTreeFragment == null) {
                    mKnowledgeTreeFragment = KnowledgeTreeFragment.getInstance()
                    transaction.add(R.id.container, mKnowledgeTreeFragment!!, "knowledge")
                } else {
                    transaction.show(mKnowledgeTreeFragment!!)
                }
            }
            FRAGMENT_NAVIGATION // 导航
            -> {
                toolbar.title = getString(R.string.navigation)
                if (mNavigationFragment == null) {
                    mNavigationFragment = NavigationFragment.getInstance()
                    transaction.add(R.id.container, mNavigationFragment!!, "navigation")
                } else {
                    transaction.show(mNavigationFragment!!)
                }
            }
            FRAGMENT_PROJECT // 项目
            -> {
                toolbar.title = getString(R.string.project)
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container, mProjectFragment!!, "project")
                } else {
                    transaction.show(mProjectFragment!!)
                }
            }
            FRAGMENT_WECHAT // 公众号
            -> {
                toolbar.title = getString(R.string.wechat)
                if (mWeChatFragment == null) {
                    mWeChatFragment = WeChatFragment.getInstance()
                    transaction.add(R.id.container, mWeChatFragment!!, "wechat")
                } else {
                    transaction.show(mWeChatFragment!!)
                }
            }
        }
        transaction.commit()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mKnowledgeTreeFragment?.let { transaction.hide(it) }
        mNavigationFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
        mWeChatFragment?.let { transaction.hide(it) }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.action_home -> {
                        showFragment(FRAGMENT_HOME)
                        true
                    }
                    R.id.action_knowledge_system -> {
                        showFragment(FRAGMENT_KNOWLEDGE)
                        true
                    }
                    R.id.action_navigation -> {
                        showFragment(FRAGMENT_NAVIGATION)
                        true
                    }
                    R.id.action_project -> {
                        showFragment(FRAGMENT_PROJECT)
                        true
                    }
                    R.id.action_wechat -> {
                        showFragment(FRAGMENT_WECHAT)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_collect -> {
                        if (isLogin) {
                            Intent(this@MainActivity, CommonActivity::class.java).run {
                                putExtra(Constant.TYPE_KEY, Constant.Type.COLLECT_TYPE_KEY)
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_setting -> {
                        Intent(this@MainActivity, SettingActivity::class.java).run {
                            // putExtra(Constant.TYPE_KEY, Constant.Type.SETTING_TYPE_KEY)
                            startActivity(this)
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_about_us -> {
                        Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                            startActivity(this)
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_logout -> {
                        logout()
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_night_mode -> {
                        if (SettingUtil.getIsNightMode()) {
                            SettingUtil.setIsNightMode(false)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        } else {
                            SettingUtil.setIsNightMode(true)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                        recreate()
                    }
                    R.id.nav_todo -> {
                        if (isLogin) {
                            Intent(this@MainActivity, TodoActivity::class.java).run {
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }
                        }
                        // drawer_layout.closeDrawer(GravityCompat.START)
                    }
                }
                true
            }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mHomeFragment != null) {
                fragmentTransaction.remove(mHomeFragment!!)
            }
            if (mKnowledgeTreeFragment != null) {
                fragmentTransaction.remove(mKnowledgeTreeFragment!!)
            }
            if (mNavigationFragment != null) {
                fragmentTransaction.remove(mNavigationFragment!!)
            }
            if (mProjectFragment != null) {
                fragmentTransaction.remove(mProjectFragment!!)
            }
            if (mWeChatFragment != null) {
                fragmentTransaction.remove(mWeChatFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

    /**
     * 退出登录 Dialog
     */
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this@MainActivity, resources.getString(R.string.logout_ing))
    }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
                DialogInterface.OnClickListener { _, _ ->
                    mDialog.show()
                    mPresenter?.logout()
                }).show()
    }

    override fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                // CookieManager().clearAllCookies()
                Preference.clearPreference()
                uiThread {
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }
    }

    /**
     * FAB 监听
     */
    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                mHomeFragment?.scrollToTop()
            }
            FRAGMENT_KNOWLEDGE -> {
                mKnowledgeTreeFragment?.scrollToTop()
            }
            FRAGMENT_NAVIGATION -> {
                mNavigationFragment?.scrollToTop()
            }
            FRAGMENT_PROJECT -> {
                mProjectFragment?.scrollToTop()
            }
            FRAGMENT_WECHAT -> {
                mWeChatFragment?.scrollToTop()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeFragment = null
        mNavigationFragment = null
        mKnowledgeTreeFragment = null
        mProjectFragment = null
        mWeChatFragment = null
        nav_username = null
    }

}
