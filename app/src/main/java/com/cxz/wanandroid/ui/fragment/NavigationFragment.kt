package com.cxz.wanandroid.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.NavigationAdapter
import com.cxz.wanandroid.adapter.NavigationTabAdapter
import com.cxz.wanandroid.base.BaseMvpFragment
import com.cxz.wanandroid.mvp.contract.NavigationContract
import com.cxz.wanandroid.mvp.model.bean.NavigationBean
import com.cxz.wanandroid.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_navigation.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * Created by chenxz on 2018/5/13.
 */
class NavigationFragment : BaseMvpFragment<NavigationContract.View, NavigationContract.Presenter>(), NavigationContract.View {

    companion object {
        fun getInstance(): NavigationFragment = NavigationFragment()
    }

    /**
     * datas
     */
    private var datas = mutableListOf<NavigationBean>()

    /**
     * linearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * NavigationAdapter
     */
    private val navigationAdapter: NavigationAdapter by lazy {
        NavigationAdapter(activity, datas)
    }

    private var bScroll: Boolean = false
    private var currentIndex: Int = 0
    private var bClickTab: Boolean = false

    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun createPresenter(): NavigationContract.Presenter = NavigationPresenter()

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }

        navigationAdapter.run {
            bindToRecyclerView(recyclerView)
        }

        leftRightLink()
    }

    /**
     * Left TabLayout and Right RecyclerView Link
     */
    private fun leftRightLink() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (bScroll && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecyclerView()
                }
                rightLinkLeft(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (bScroll) {
                    scrollRecyclerView()
                }
            }
        })

        navigation_tab_layout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                bClickTab = true
                selectTab(position)
            }
        })

    }

    private fun scrollRecyclerView() {
        bScroll = false
        val indexDistance: Int = currentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < recyclerView!!.childCount) {
            val top: Int = recyclerView.getChildAt(indexDistance).top
            recyclerView.smoothScrollBy(0, top)
        }
    }

    /**
     * Right RecyclerView link Left TabLayout
     *
     * @param newState RecyclerView Scroll State
     */
    private fun rightLinkLeft(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (bClickTab) {
                bClickTab = false
                return
            }
            val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
            if (firstPosition != currentIndex) {
                currentIndex = firstPosition
                setChecked(currentIndex)
            }
        }
    }

    /**
     * Smooth Right RecyclerView to Select Left TabLayout
     *
     * @param position checked position
     */
    private fun setChecked(position: Int) {
        if (bClickTab) {
            bClickTab = false
        } else {
            navigation_tab_layout.setTabSelected(currentIndex)
        }
        currentIndex = position
    }

    /**
     * Select Left TabLayout to Smooth Right RecyclerView
     */
    private fun selectTab(position: Int) {
        currentIndex = position
        recyclerView.stopScroll()
        smoothScrollToPosition(position)
    }

    private fun smoothScrollToPosition(position: Int) {
        val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPosition -> {
                recyclerView.smoothScrollToPosition(position)
            }
            position <= lastPosition -> {
                val top: Int = recyclerView.getChildAt(position - firstPosition).top
                recyclerView.smoothScrollBy(0, top)
            }
            else -> {
                recyclerView.smoothScrollToPosition(position)
                bScroll = true
            }
        }
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.requestNavigationList()
    }

    override fun setNavigationData(list: List<NavigationBean>) {
        list.let {
            navigation_tab_layout.run {
                setTabAdapter(NavigationTabAdapter(activity!!.applicationContext, list))
            }
            navigationAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
        if (navigationAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    fun scrollToTop() {
        navigation_tab_layout.setTabSelected(0)
    }

}