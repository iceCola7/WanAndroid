package com.cxz.wanandroid.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.RankAdapter
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.ext.setNewOrAddData
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
    private var pageSize = 20

    /**
     * PageNum
     */
    private var pageNum = 1

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val rankAdapter: RankAdapter by lazy {
        RankAdapter()
    }

    override fun createPresenter(): RankContract.Presenter = RankPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_rank

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
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
        swipeRefreshLayout.setOnRefreshListener {
            pageNum = 1
            mPresenter?.getRankList(pageNum)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@RankActivity)
            adapter = rankAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }
        rankAdapter.run {
            loadMoreModule.setOnLoadMoreListener {
                pageNum++
                swipeRefreshLayout.isRefreshing = false
                mPresenter?.getRankList(pageNum)
            }
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getRankList(1)
    }

    override fun showRankList(body: BaseListResponseBody<CoinInfoBean>) {
        rankAdapter.setNewOrAddData(pageNum == 1, body.datas)
        if (rankAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }
}
