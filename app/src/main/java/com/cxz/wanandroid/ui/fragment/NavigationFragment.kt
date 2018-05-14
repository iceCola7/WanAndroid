package com.cxz.wanandroid.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.NavigationAdapter
import com.cxz.wanandroid.adapter.NavigationTabAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.mvp.contract.NavigationContract
import com.cxz.wanandroid.mvp.model.bean.NavigationBean
import com.cxz.wanandroid.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_navigation.*

/**
 * Created by chenxz on 2018/5/13.
 */
class NavigationFragment : BaseFragment(), NavigationContract.View {

    private val mPresenter by lazy {
        NavigationPresenter()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
    }

    companion object {
        fun getInstance(): NavigationFragment = NavigationFragment()
    }

    /**
     * datas
     */
    private var datas = mutableListOf<NavigationBean>()

    /**
     * linearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * NavigationAdapter
     */
    private val navigationAdapter: NavigationAdapter by lazy {
        NavigationAdapter(activity, datas)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView() {
        mPresenter.attachView(this)

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
            itemAnimator = DefaultItemAnimator()
        }

        navigationAdapter.run {
            bindToRecyclerView(recyclerView)
        }
    }

    override fun lazyLoad() {
        mPresenter.requestNavigationList()
    }

    override fun setNavigationData(list: List<NavigationBean>) {
        list.let {
            navigation_tab_layout.run {
                setTabAdapter(NavigationTabAdapter(activity!!.applicationContext, list))
            }
            navigationAdapter.run {
                replaceData(it)

                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}