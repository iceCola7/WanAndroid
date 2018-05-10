package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.KnowledgeAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.mvp.contract.KnowledgeContract
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.cxz.wanandroid.mvp.presenter.KnowledgePresenter
import com.cxz.wanandroid.ui.activity.TypeKnowledgeActivity
import com.cxz.wanandroid.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgeFragment : BaseFragment(), KnowledgeContract.View {

    companion object {
        fun getInstance(): KnowledgeFragment = KnowledgeFragment()
    }

    private val mPresenter by lazy {
        KnowledgePresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_knowledge

    private val datas = mutableListOf<KnowledgeTreeBody>()

    private val knowledgeAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter(activity, datas)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            RecyclerViewItemDecoration(it, LinearLayoutManager.VERTICAL)
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = knowledgeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        knowledgeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = this@KnowledgeFragment.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
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
        knowledgeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        lists.let {
            knowledgeAdapter.run {
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
            Intent(activity, TypeKnowledgeActivity::class.java).run {
                putExtra(Contanst.CONTENT_TITLE_KEY, data.name)
                putExtra(Contanst.CONTENT_DATA_KEY, data)
                startActivity(this)
            }
        }
    }

}