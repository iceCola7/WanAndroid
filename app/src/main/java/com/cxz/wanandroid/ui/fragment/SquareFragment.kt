package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.HomeAdapter
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.BaseMvpFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showSnackMsg
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.SquareContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.presenter.SquarePresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.ui.activity.LoginActivity
import com.cxz.wanandroid.utils.NetWorkUtil
import com.cxz.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @author chenxz
 * @date 2019/11/16
 * @desc 广场
 */
class SquareFragment : BaseMvpFragment<SquareContract.View, SquarePresenter>(), SquareContract.View {

    private var pageSize = 20

    private val datas = mutableListOf<Article>()

    private val mAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }

    private var isRefresh = true

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    companion object {
        fun getInstance(): SquareFragment = SquareFragment()
    }

    override fun createPresenter(): SquarePresenter = SquarePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_square

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            mAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        mAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(SpaceItemDecoration(activity!!))
        }
        mAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@SquareFragment.onItemClickListener
            onItemChildClickListener = this@SquareFragment.onItemChildClickListener
        }


    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getSquareList(0)
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showSquareList(body: ArticleResponseBody) {
        body.datas.let {
            mAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                pageSize = it.size
                if (pageSize < body.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        mAdapter.setEnableLoadMore(false)
        mPresenter?.getSquareList(0)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = mAdapter.data.size / pageSize
        mPresenter?.getSquareList(page)
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.link)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
                if (datas.size != 0) {
                    val data = datas[position]
                    when (view.id) {
                        R.id.iv_like -> {
                            if (isLogin) {
                                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                                    showSnackMsg(resources.getString(R.string.no_network))
                                    return@OnItemChildClickListener
                                }
                                val collect = data.collect
                                data.collect = !collect
                                mAdapter.setData(position, data)
                                if (collect) {
                                    mPresenter?.cancelCollectArticle(data.id)
                                } else {
                                    mPresenter?.addCollectArticle(data.id)
                                }
                            } else {
                                Intent(activity, LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showToast(resources.getString(R.string.login_tint))
                            }
                        }
                    }
                }
            }

}