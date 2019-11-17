package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.KnowledgeTreeAdapter
import com.cxz.wanandroid.base.BaseMvpListFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.mvp.contract.KnowledgeTreeContract
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.cxz.wanandroid.mvp.presenter.KnowledgeTreePresenter
import com.cxz.wanandroid.ui.activity.KnowledgeActivity
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * Created by chenxz on 2018/5/8.
 */
class KnowledgeTreeFragment : BaseMvpListFragment<KnowledgeTreeContract.View, KnowledgeTreeContract.Presenter>(), KnowledgeTreeContract.View {

    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<KnowledgeTreeBody>()

    /**
     * Adapter
     */
    private val mAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity, datas)
    }

    override fun createPresenter(): KnowledgeTreeContract.Presenter = KnowledgeTreePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)

        recyclerView.adapter = mAdapter

        mAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = this@KnowledgeTreeFragment.onItemClickListener
            // setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeTree()
    }

    override fun onRefreshList() {
        mPresenter?.requestKnowledgeTree()
    }

    override fun onLoadMoreList() {
    }

    override fun hideLoading() {
        super.hideLoading()
        mAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mAdapter.loadMoreFail()
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
            mAdapter.run {
                replaceData(lists)
            }
        }
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
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