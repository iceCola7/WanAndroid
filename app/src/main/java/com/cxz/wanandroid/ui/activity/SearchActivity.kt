package com.cxz.wanandroid.ui.activity

import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.SearchHistoryAdapter
import com.cxz.wanandroid.base.BaseMvpSwipeBackActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.SearchContract
import com.cxz.wanandroid.mvp.model.bean.HotSearchBean
import com.cxz.wanandroid.mvp.model.bean.SearchHistoryBean
import com.cxz.wanandroid.mvp.presenter.SearchPresenter
import com.cxz.wanandroid.utils.CommonUtil
import com.cxz.wanandroid.utils.DisplayManager
import com.cxz.wanandroid.widget.RecyclerViewItemDecoration
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : BaseMvpSwipeBackActivity<SearchContract.View, SearchContract.Presenter>(), SearchContract.View {

    override fun createPresenter(): SearchContract.Presenter = SearchPresenter()

    /**
     * 热搜数据
     */
    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    /**
     * datas
     */
    private val datas = mutableListOf<SearchHistoryBean>()

    /**
     * SearchHistoryAdapter
     */
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(this, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        RecyclerViewItemDecoration(this)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_search_flow_layout.run {
            setOnTagClickListener { view, position, parent ->
                if (mHotSearchDatas.size > 0) {
                    val hotSearchBean = mHotSearchDatas[position]
                    goToSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }

        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
            // addItemDecoration(recyclerViewItemDecoration)
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_history_search)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.search_empty_view)
        }

        search_history_clear_all_tv.setOnClickListener {
            datas.clear()
            searchHistoryAdapter.replaceData(datas)
            mPresenter?.clearAllHistory()
        }

        mPresenter?.getHotSearchData()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.queryHistory()
    }

    override fun start() {
    }

    private fun goToSearchList(key: String) {
        mPresenter?.saveSearchKey(key)
        Intent(this, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, Constant.Type.SEARCH_TYPE_KEY)
            putExtra(Constant.SEARCH_KEY, key)
            startActivity(this)
        }
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas) {
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: HotSearchBean?): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv,
                        hot_search_flow_layout, false) as TextView
                val padding: Int = DisplayManager.dip2px(10F)!!
                tv.setPadding(padding, padding, padding, padding)
                tv.text = hotSearchBean?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.setOnQueryTextListener(queryTextListener)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * OnQueryTextListener
     */
    private val queryTextListener = object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            goToSearchList(query.toString())
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            goToSearchList(item.key)
        }
    }

    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
                if (searchHistoryAdapter.data.size != 0) {
                    val item = searchHistoryAdapter.data[position]
                    when (view.id) {
                        R.id.iv_clear -> {
                            mPresenter?.deleteById(item.id)
                            searchHistoryAdapter.remove(position)
                        }
                    }
                }
            }

}
