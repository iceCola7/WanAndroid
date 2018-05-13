package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.KnowledgeAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.KnowledgeContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.presenter.KnowledgePresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by chenxz on 2018/5/10.
 */
class KnowledgeFragment : BaseFragment(), KnowledgeContract.View {

    companion object {
        fun newInstance(cid: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Contanst.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * cid
     */
    private var cid: Int = 0

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Home Adapter
     */
    private val knowledgeAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter(activity, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val mPresenter: KnowledgePresenter by lazy {
        KnowledgePresenter()
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        knowledgeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        knowledgeAdapter.run {
            setEnableLoadMore(false)
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

    override fun attachLayoutRes(): Int = R.layout.fragment_knowledge

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments!!.getInt(Contanst.CONTENT_CID_KEY)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        knowledgeAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@KnowledgeFragment.onItemClickListener
            onItemChildClickListener = this@KnowledgeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
    }

    override fun lazyLoad() {
        mPresenter.requestKnowledgeList(0, cid)
    }

    override fun setKnowledgeList(articles: ArticleResponseBody) {
        articles.datas.let {
            knowledgeAdapter.run {
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
        knowledgeAdapter.setEnableLoadMore(false)
        mPresenter.requestKnowledgeList(0, cid)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = knowledgeAdapter.data.size / 20 + 1
        mPresenter.requestKnowledgeList(page, cid)
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Contanst.CONTENT_URL_KEY, data.link)
                putExtra(Contanst.CONTENT_TITLE_KEY, data.title)
                putExtra(Contanst.CONTENT_ID_KEY, data.id)
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
                    }
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}