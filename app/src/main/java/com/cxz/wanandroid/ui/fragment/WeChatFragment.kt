package com.cxz.wanandroid.ui.fragment

import android.support.design.widget.TabLayout
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.WeChatPagerAdapter
import com.cxz.wanandroid.base.BaseMvpFragment
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.mvp.contract.WeChatContract
import com.cxz.wanandroid.mvp.model.bean.WXChapterBean
import com.cxz.wanandroid.mvp.presenter.WeChatPresenter
import com.cxz.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_wechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author chenxz
 * @date 2018/10/28
 * @desc 公众号
 */
class WeChatFragment : BaseMvpFragment<WeChatContract.View, WeChatContract.Presenter>(), WeChatContract.View {

    companion object {
        fun getInstance(): WeChatFragment = WeChatFragment()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<WXChapterBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: WeChatPagerAdapter by lazy {
        WeChatPagerAdapter(datas, childFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_wechat

    override fun createPresenter(): WeChatContract.Presenter = WeChatPresenter()

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            // TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        refreshColor(ColorEvent(true))

    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.getWXChapters()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun doReConnected() {
        if (datas.size == 0) {
            super.doReConnected()
        }
    }

    override fun showWXChapters(chapters: MutableList<WXChapterBean>) {
        chapters.let {
            datas.addAll(it)
            doAsync {
                Thread.sleep(10)
                uiThread {
                    viewPager.run {
                        adapter = viewPagerAdapter
                        offscreenPageLimit = datas.size
                    }
                }
            }
        }
        if (chapters.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) {
            return
        }
        val fragment: KnowledgeFragment = viewPagerAdapter.getItem(viewPager.currentItem) as KnowledgeFragment
        fragment.scrollToTop()
    }

}