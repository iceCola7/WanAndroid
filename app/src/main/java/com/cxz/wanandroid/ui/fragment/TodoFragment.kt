package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.TodoAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.model.bean.TodoBean
import com.cxz.wanandroid.mvp.model.bean.TodoResponseBody
import com.cxz.wanandroid.mvp.presenter.TodoPresenter
import com.cxz.wanandroid.widget.SpaceItemDecoration
import com.cxz.wanandroid.widget.StickyHeaderDecoration
import com.cxz.wanandroid.widget.SwipeItemLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * Created by chenxz on 2018/8/6.
 */

class TodoFragment : BaseFragment(), TodoContract.View {

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.TODO_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * Presenter
     */
    private val mPresenter by lazy {
        TodoPresenter()
    }
    /**
     * is Refresh
     */
    private var isRefresh = true

    private var mType: Int = 0

    private val datas = mutableListOf<TodoBean>()

    private val todoAdapter: TodoAdapter by lazy {
        TodoAdapter(datas)
    }

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

    private val stickyHeaderDecoration = object : StickyHeaderDecoration() {
        override fun getHeaderName(pos: Int): String {
            return if (todoAdapter.data.size == 0) "" else todoAdapter.data[pos].dateStr
        }
    }

    override fun showLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            todoAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            todoAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        todoAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showToast(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
        mPresenter.attachView(this)
        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        stickyHeaderDecoration.setOnDecorationHeadDraw {
            val headView = layoutInflater.inflate(R.layout.item_sticky_header, null)
            if (todoAdapter.data.size > 0) {
                val tv_header = headView.findViewById<TextView>(R.id.tv_header)
                tv_header.text = todoAdapter.data[it].dateStr
            }
            headView
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = todoAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
            addItemDecoration(stickyHeaderDecoration)
        }

        todoAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@TodoFragment.onItemClickListener
            onItemChildClickListener = this@TodoFragment.onItemChildClickListener
            // setEmptyView(R.layout.fragment_empty_layout)
        }

    }

    override fun lazyLoad() {
        mPresenter.getNoTodoList(1, mType)
    }

    override fun showNoTodoList(todoResponseBody: TodoResponseBody) {
        todoResponseBody.datas.let {
            todoAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < todoResponseBody.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }

            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        todoAdapter.setEnableLoadMore(false)
        mPresenter.getNoTodoList(1, mType)
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = todoAdapter.data.size / 20 + 1
        mPresenter.getNoTodoList(page, mType)
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]

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