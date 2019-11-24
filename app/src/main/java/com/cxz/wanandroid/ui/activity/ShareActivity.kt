package com.cxz.wanandroid.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.ShareAdapter
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshShareEvent
import com.cxz.wanandroid.ext.showSnackMsg
import com.cxz.wanandroid.mvp.contract.ShareContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ShareResponseBody
import com.cxz.wanandroid.mvp.presenter.SharePresenter
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.utils.NetWorkUtil
import com.cxz.wanandroid.widget.SpaceItemDecoration
import com.cxz.wanandroid.widget.SwipeItemLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc 我的分享
 */
class ShareActivity : BaseMvpSwipeBackActivity<ShareContract.View, SharePresenter>(), ShareContract.View {

    private var pageSize = 20

    private var isRefresh = true

    private val datas = mutableListOf<Article>()

    private val shareAdapter: ShareAdapter by lazy {
        ShareAdapter(datas)
    }

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            shareAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        if (isRefresh) {
            shareAdapter.setEnableLoadMore(true)
        } else {
            shareAdapter.loadMoreFail()
        }
    }

    override fun createPresenter(): SharePresenter = SharePresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_share

    override fun useEventBus(): Boolean = true

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.my_share)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@ShareActivity)
            adapter = shareAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(SpaceItemDecoration(this@ShareActivity))
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this@ShareActivity))
        }
        shareAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ShareActivity.onItemClickListener
            onItemChildClickListener = this@ShareActivity.onItemChildClickListener
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getShareList(1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshShare(event: RefreshShareEvent) {
        if (event.isRefresh) {
            start()
        }
    }

    override fun showShareList(body: ShareResponseBody) {
        body.shareArticles.datas.let {
            shareAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                pageSize = body.shareArticles.size
                if (body.shareArticles.over) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
        if (shareAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showDeleteArticle(success: Boolean) {
        if (success) {
            showMsg(getString(R.string.share_article_delete))
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.cancel_collect_success))
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        shareAdapter.setEnableLoadMore(false)
        mPresenter?.getShareList(1)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = shareAdapter.data.size / pageSize + 1
        mPresenter?.getShareList(page)
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.isNotEmpty()) {
            val data = datas[position]
        }
    }

    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                    showSnackMsg(resources.getString(R.string.no_network))
                    return@OnItemChildClickListener
                }
                if (datas.isNotEmpty()) {
                    val data = datas[position]
                    when (view.id) {
                        R.id.rl_content -> {
                            ContentActivity.start(this, data.id, data.title, data.link)
                        }
                        R.id.iv_like -> {
                            if (isLogin) {
                                val collect = data.collect
                                data.collect = !collect
                                shareAdapter.setData(position, data)
                                if (collect) {
                                    mPresenter?.cancelCollectArticle(data.id)
                                } else {
                                    mPresenter?.addCollectArticle(data.id)
                                }
                            } else {
                                Intent(this, LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showMsg(resources.getString(R.string.login_tint))
                            }
                        }
                        R.id.btn_delete -> {
                            DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_delete),
                                    DialogInterface.OnClickListener { _, _ ->
                                        mPresenter?.deleteShareArticle(data.id)
                                        shareAdapter.remove(position)
                                    }).show()
                        }
                    }
                }
            }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add -> {
                Intent(this@ShareActivity, CommonActivity::class.java).run {
                    putExtra(Constant.TYPE_KEY, Constant.Type.SHARE_ARTICLE_TYPE_KEY)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}