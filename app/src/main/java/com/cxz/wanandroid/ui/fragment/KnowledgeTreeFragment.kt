package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.KnowledgeTreeAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.KnowledgeTreeContract
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.cxz.wanandroid.mvp.presenter.KnowledgeTreePresenter
import com.cxz.wanandroid.ui.activity.KnowledgeActivity
import com.cxz.wanandroid.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgeTreeFragment : BaseFragment(), KnowledgeTreeContract.View {

    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    private val mPresenter by lazy {
        KnowledgeTreePresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    /**
     * datas
     */
    private val datas = mutableListOf<KnowledgeTreeBody>()

    /**
     * Adapter
     */
    private val knowledgeTreeAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity, datas)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            RecyclerViewItemDecoration(it, LinearLayoutManager.VERTICAL)
        }
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun initView() {
        mPresenter.attachView(this)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeTreeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        knowledgeTreeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = this@KnowledgeTreeFragment.onItemClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    override fun lazyLoad() {
        mPresenter.requestKnowledgeTree()
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        knowledgeTreeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        knowledgeTreeAdapter.run {
            loadMoreFail()
        }
        showToast(errorMsg)
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

    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        lists.let {
            knowledgeTreeAdapter.run {
                replaceData(lists)
            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        mPresenter.requestKnowledgeTree()
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            Intent(activity, KnowledgeActivity::class.java).run {
                putExtra(Constant.CONTENT_TITLE_KEY, data.name)
                putExtra(Constant.CONTENT_DATA_KEY, data)
                startActivity(this)
            }
        }
    }

}