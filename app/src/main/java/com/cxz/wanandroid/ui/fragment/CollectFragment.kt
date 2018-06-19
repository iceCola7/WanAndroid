package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.CollectAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.CollectContract
import com.cxz.wanandroid.mvp.model.bean.CollectionArticle
import com.cxz.wanandroid.mvp.model.bean.CollectionResponseBody
import com.cxz.wanandroid.mvp.presenter.CollectPresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by chenxz on 2018/6/9.
 */
class CollectFragment : BaseFragment(), CollectContract.View {

    companion object {
        fun getInstance(bundle: Bundle): CollectFragment {
            val fragment = CollectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter: CollectPresenter by lazy {
        CollectPresenter()
    }

    override fun showLoading() {
        swipeRefreshLayout.run {
            if (!isRefreshing) {
                isRefreshing = true
            }
        }
    }

    override fun hideLoading() {
        swipeRefreshLayout.run {
            if (isRefreshing) {
                isRefreshing = false
            }
        }
        collectAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        collectAdapter.run {
            setEnableLoadMore(false)
            loadMoreFail()
        }
        showToast(errorMsg)
    }

    /**
     * datas
     */
    private val datas = mutableListOf<CollectionArticle>()
    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Collect Adapter
     */
    private val collectAdapter: CollectAdapter by lazy {
        CollectAdapter(activity, datas = datas)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = collectAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        collectAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@CollectFragment.onItemClickListener
            onItemChildClickListener = this@CollectFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }

    }

    override fun lazyLoad() {
        mPresenter.getCollectList(0)
    }

    override fun showRemoveCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun setCollectList(articles: CollectionResponseBody<CollectionArticle>) {
        articles.datas.let {
            collectAdapter.run {
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val over = articles.over
                if (!over) {
                    loadMoreComplete()
                    setEnableLoadMore(true)
                } else {
                    loadMoreComplete()
                    loadMoreEnd()
                    setEnableLoadMore(false)
                }
            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        collectAdapter.setEnableLoadMore(false)
        mPresenter.getCollectList(0)
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        swipeRefreshLayout.isRefreshing = false
        val page = collectAdapter.data.size / 20 + 1
        mPresenter.getCollectList(page)
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
                            collectAdapter.remove(position)
                            mPresenter.removeCollectArticle(data.id, data.originId)
                        }
                    }
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}