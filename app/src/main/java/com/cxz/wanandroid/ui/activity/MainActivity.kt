package com.cxz.wanandroid.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.event.LoginEvent
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.ui.fragment.HomeFragment
import com.cxz.wanandroid.ui.fragment.KnowledgeTreeFragment
import com.cxz.wanandroid.ui.fragment.NavigationFragment
import com.cxz.wanandroid.ui.fragment.ProjectFragment
import com.cxz.wanandroid.ui.setting.SettingActivity
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.utils.Preference
import com.cxz.wanandroid.utils.SettingUtil
import com.cxz.wanandroid.widget.helper.BottomNavigationViewHelper
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity() {

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE = 0x02
    private val FRAGMENT_NAVIGATION = 0x03
    private val FRAGMENT_PROJECT = 0x04

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment: HomeFragment? = null
    private var mKnowledgeTreeFragment: KnowledgeTreeFragment? = null
    private var mNavigationFragment: NavigationFragment? = null
    private var mProjectFragment: ProjectFragment? = null

    /**
     * local username
     */
    private val username: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * username TextView
     */
    private lateinit var nav_username: TextView

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun enableSwipeBack(): Boolean = false

    override fun initData() {
        Beta.checkUpgrade(false, false)
    }

    override fun useEventBus(): Boolean = true

    override fun initView() {
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            BottomNavigationViewHelper.disableShiftMode(this)
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.action_home
        }

        initDrawerLayout()

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }
            setOnClickListener({
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                } else {

                }
            })
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
            nav_username.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
        } else {
            nav_username.text = resources.getString(R.string.login)
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
            val color = SettingUtil.getColor()
            nav_view.getHeaderView(0).setBackgroundColor(color)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(color)
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
                    transaction.add(R.id.container, mHomeFragment, "home")
                } else {
                    transaction.show(mHomeFragment)
                }
            }
            FRAGMENT_KNOWLEDGE // 知识体系
            -> {
                toolbar.title = getString(R.string.knowledge_system)
                if (mKnowledgeTreeFragment == null) {
                    mKnowledgeTreeFragment = KnowledgeTreeFragment.getInstance()
                    transaction.add(R.id.container, mKnowledgeTreeFragment, "knowledge")
                } else {
                    transaction.show(mKnowledgeTreeFragment)
                }
            }
            FRAGMENT_NAVIGATION // 导航
            -> {
                toolbar.title = getString(R.string.navigation)
                if (mNavigationFragment == null) {
                    mNavigationFragment = NavigationFragment.getInstance()
                    transaction.add(R.id.container, mNavigationFragment, "navigation")
                } else {
                    transaction.show(mNavigationFragment)
                }
            }
            FRAGMENT_PROJECT // 项目
            -> {
                toolbar.title = getString(R.string.project)
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container, mProjectFragment, "project")
                } else {
                    transaction.show(mProjectFragment)
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
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_setting -> {
                        Intent(this@MainActivity, SettingActivity::class.java).run {
                            // putExtra(Constant.TYPE_KEY, Constant.Type.SETTING_TYPE_KEY)
                            startActivity(this)
                        }
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_about_us -> {
                        Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                            startActivity(this)
                        }
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    R.id.nav_logout -> {
                        logout()
                        drawer_layout.closeDrawer(GravityCompat.START)
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
                }
                true
            }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
                DialogInterface.OnClickListener { _, _ ->
                    val dialog = DialogUtil.getWaitDialog(this@MainActivity, "")
                    dialog.show()
                    doAsync {
                        // CookieManager().clearAllCookies()
                        Preference.clearPreference()
                        uiThread {
                            dialog.dismiss()
                            showToast(resources.getString(R.string.logout_success))
                            isLogin = false
                            EventBus.getDefault().post(LoginEvent(false))
                            Intent(this@MainActivity, LoginActivity::class.java).run {
                                startActivity(this)
                            }
                        }
                    }
                }).show()
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

}
