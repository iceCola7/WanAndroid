package com.cxz.wanandroid.ui.activity

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.RankAdapter
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.mvp.contract.RankContract
import com.cxz.wanandroid.mvp.model.bean.BaseListResponseBody
import com.cxz.wanandroid.mvp.model.bean.CoinInfoBean
import com.cxz.wanandroid.mvp.presenter.RankPresenter
import com.cxz.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * 排行榜页面
 */
class RankActivity : BaseMvpSwipeBackActivity<RankContract.View, RankContract.Presenter>(), RankContract.View {

    /**
     * 每页数据的个数
     */
    private var pageSize = 20;

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val rankAdapter: RankAdapter by lazy {
        RankAdapter()
    }

    /**
     * is Refresh
     */
    private var isRefresh = true

    override fun createPresenter(): RankContract.Presenter = RankPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_rank

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            rankAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        if (isRefresh) {
            rankAdapter.setEnableLoadMore(true)
        } else {
            rankAdapter.loadMoreFail()
        }
    }

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.score_list)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@RankActivity)
            adapter = rankAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }
        rankAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getRankList(1)
    }

    override fun showRankList(body: BaseListResponseBody<CoinInfoBean>) {
        body.datas.let {
            rankAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                pageSize = body.size
                if (body.over) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
        if (rankAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        rankAdapter.setEnableLoadMore(false)
        mPresenter?.getRankList(1)
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = rankAdapter.data.size / pageSize + 1
        mPresenter?.getRankList(page)
    }

}
