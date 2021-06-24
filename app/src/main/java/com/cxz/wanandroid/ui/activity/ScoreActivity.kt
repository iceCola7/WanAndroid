package com.cxz.wanandroid.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.ScoreAdapter
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.ext.setNewOrAddData
import com.cxz.wanandroid.mvp.contract.ScoreContract
import com.cxz.wanandroid.mvp.model.bean.BaseListResponseBody
import com.cxz.wanandroid.mvp.model.bean.UserScoreBean
import com.cxz.wanandroid.mvp.presenter.ScorePresenter
import com.cxz.wanandroid.widget.SpaceItemDecoration
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_score.*


/**
 * 我的积分页面
 */
class ScoreActivity : BaseMvpSwipeBackActivity<ScoreContract.View, ScoreContract.Presenter>(), ScoreContract.View {

    /**
     * 每页展示的个数
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

    private val scoreAdapter: ScoreAdapter by lazy {
        ScoreAdapter()
    }

    private var contentHeight = 0F

    override fun createPresenter(): ScoreContract.Presenter = ScorePresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_score

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
            title = getString(R.string.score_detail)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        App.userInfo?.let {
            tv_score.text = it.coinCount.toString()
        }

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@ScoreActivity)
            adapter = scoreAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }
        scoreAdapter.run {
            loadMoreModule.setOnLoadMoreListener(onRequestLoadMoreListener)
        }

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            contentHeight = rl_content.height.toFloat()
            val alpha = 1 - (-verticalOffset) / (contentHeight)
            rl_content.alpha = alpha
        })

    }

    override fun initColor() {
        super.initColor()
        rl_content.setBackgroundColor(mThemeColor)
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getUserScoreList(1)
    }

    override fun showUserScoreList(body: BaseListResponseBody<UserScoreBean>) {
        scoreAdapter.setNewOrAddData(pageNum == 1, body.datas)
        if (scoreAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        pageNum = 1
        mPresenter?.getUserScoreList(pageNum)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = OnLoadMoreListener {
        pageNum++
        swipeRefreshLayout.isRefreshing = false
        mPresenter?.getUserScoreList(pageNum)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_score, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_help -> {
                val url = "https://www.wanandroid.com/blog/show/2653"
                ContentActivity.start(this@ScoreActivity, 2653, "", url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
