package com.cxz.wanandroid.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.LoginEvent
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.ui.fragment.HomeFragment
import com.cxz.wanandroid.ui.fragment.KnowledgeTreeFragment
import com.cxz.wanandroid.ui.fragment.NavigationFragment
import com.cxz.wanandroid.ui.fragment.ProjectFragment
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.utils.Preference
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
     * check login
     */
    private var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    /**
     * local username
     */
    private val username: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * username TextView
     */
    private lateinit var nav_username: TextView

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
            //StatusBarUtil.setPaddingSmart(this@MainActivity, toolbar)
        }

        bottom_navigation.run {
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
                    }
                    R.id.nav_setting -> {

                    }
                    R.id.nav_about_us -> {

                    }
                    R.id.nav_logout -> {
                        logout()
                    }
                }
                // drawer_layout.closeDrawer(GravityCompat.START)
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
        EventBus.getDefault().unregister(this)
    }

}
