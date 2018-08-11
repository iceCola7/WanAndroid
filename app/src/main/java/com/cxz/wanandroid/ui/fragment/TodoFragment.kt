package com.cxz.wanandroid.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.TodoAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.TodoEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.TodoContract
import com.cxz.wanandroid.mvp.model.bean.TodoDataBean
import com.cxz.wanandroid.mvp.model.bean.TodoResponseBody
import com.cxz.wanandroid.mvp.presenter.TodoPresenter
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.widget.SpaceItemDecoration
import com.cxz.wanandroid.widget.SwipeItemLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    /**
     * 是否是已完成 false->待办 true->已完成
     */
    private var bDone: Boolean = false

    private val datas = mutableListOf<TodoDataBean>()

    private val mAdapter: TodoAdapter by lazy {
        TodoAdapter(R.layout.item_todo_list, R.layout.item_sticky_header, datas)
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

    override fun showLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            mAdapter.run {
                setEnableLoadMore(true)
            }
        }
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
        mAdapter.run {
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

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
        }

        mAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@TodoFragment.onItemClickListener
            onItemChildClickListener = this@TodoFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }

    }

    override fun lazyLoad() {
        if (bDone) {
            mPresenter.getDoneList(1, mType)
        } else {
            mPresenter.getNoTodoList(1, mType)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doTodoEvent(event: TodoEvent) {
        if (mType == event.curIndex) {
            when (event.type) {
                Constant.TODO_ADD -> {

                }
                Constant.TODO_NO -> {
                    bDone = false
                    lazyLoad()
                }
                Constant.TODO_DONE -> {
                    bDone = true
                    lazyLoad()
                }
            }
        }
    }

    override fun showNoTodoList(todoResponseBody: TodoResponseBody) {
        val list = mutableListOf<TodoDataBean>()
        var bHeader = true
        todoResponseBody.datas.forEach { todoBean ->
            bHeader = true
            for (i in list.indices) {
                if (todoBean.dateStr == list[i].header) {
                    bHeader = false
                    break
                }
            }
            if (bHeader)
                list.add(TodoDataBean(true, todoBean.dateStr))
            list.add(TodoDataBean(todoBean))
        }

        list.let {
            mAdapter.run {
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

    override fun showDeleteSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.delete_success))
        }
    }

    override fun showUpdateSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.completed))
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        mAdapter.setEnableLoadMore(false)
        lazyLoad()
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = mAdapter.data.size / 20 + 1
        if (bDone) {
            mPresenter.getDoneList(page, mType)
        } else {
            mPresenter.getNoTodoList(page, mType)
        }
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
                    val data = datas[position].t
                    when (view.id) {
                        R.id.btn_delete -> {
                            activity?.let {
                                DialogUtil.getConfirmDialog(it, resources.getString(R.string.confirm_delete),
                                        DialogInterface.OnClickListener { _, _ ->
                                            mPresenter.deleteTodoById(data.id)
                                            mAdapter.remove(position)
                                        }).show()
                            }
                        }
                        R.id.btn_done -> {
                            if (bDone) {
                                mPresenter.updateTodoById(data.id, 0)
                            } else {
                                mPresenter.updateTodoById(data.id, 1)
                            }
                            mAdapter.remove(position)
                        }
                    }
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}