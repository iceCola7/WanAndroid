package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.ProjectAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.ProjectListContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.presenter.ProjectListPresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_project_list.*

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectListFragment : BaseFragment(), ProjectListContract.View {

    companion object {
        fun getInstance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Contanst.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        projectAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        projectAdapter.run {
            setEnableLoadMore(false)
            loadMoreFail()
        }
        showToast(errorMsg)
    }

    /**
     * cid
     */
    private var cid: Int = 0

    /**
     * Article datas
     */
    private val datas = mutableListOf<Article>()

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
        activity?.let { SpaceItemDecoration(it) }
    }

    /**
     * ProjectAdapter
     */
    private val projectAdapter: ProjectAdapter by lazy {
        ProjectAdapter(activity, datas)
    }

    private val mPresenter: ProjectListPresenter by lazy {
        ProjectListPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_project_list

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments!!.getInt(Contanst.CONTENT_CID_KEY)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        projectAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }

    }

    override fun lazyLoad() {
        mPresenter.requestProjectList(1, cid)
    }

    override fun setProjectList(articles: ArticleResponseBody) {
        articles.datas.let {
            projectAdapter.run {
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

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        projectAdapter.setEnableLoadMore(false)
        mPresenter.requestProjectList(1, cid)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = projectAdapter.data.size / 15 + 1
        mPresenter.requestProjectList(page, cid)
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