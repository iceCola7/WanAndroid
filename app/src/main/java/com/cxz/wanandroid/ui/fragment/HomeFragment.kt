package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.HomeAdapter
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.showSnackMsg
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner
import com.cxz.wanandroid.mvp.presenter.HomePresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.ui.activity.LoginActivity
import com.cxz.wanandroid.utils.ImageLoader
import com.cxz.wanandroid.utils.NetWorkUtil
import com.cxz.wanandroid.widget.SpaceItemDecoration
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.item_home_banner.view.*

/**
 * Created by chenxz on 2018/4/22.
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }

    /**
     * Presenter
     */
    private val mPresenter: HomePresenter by lazy {
        HomePresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * banner datas
     */
    private lateinit var bannerDatas: ArrayList<Banner>

    /**
     * banner view
     */
    private val bannerView by lazy {
        layoutInflater.inflate(R.layout.item_home_banner, null)
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
     * Home Adapter
     */
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }

    /**
     * Banner Adapter
     */
    private val bannerAdapter: BGABanner.Adapter<ImageView, String> by lazy {
        BGABanner.Adapter<ImageView, String> { bgaBanner, imageView, feedImageUrl, position ->
            ImageLoader.load(activity, feedImageUrl, imageView)
        }
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun initView() {
        mPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
            addHeaderView(bannerView)
        }

        bannerView.banner.run {
            setDelegate(bannerDelegate)
        }

    }

    override fun lazyLoad() {
        mPresenter.requestBanner()
        mPresenter.requestArticles(0)
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
        homeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        homeAdapter.run {
            setEnableLoadMore(false)
            loadMoreFail()
        }
        showToast(errorMsg)
    }

    override fun setBanner(banners: List<Banner>) {
        bannerDatas = banners as ArrayList<Banner>
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        Observable.fromIterable(banners)
                .subscribe({ list ->
                    bannerFeedList.add(list.imagePath)
                    bannerTitleList.add(list.title)
                })
        bannerView.banner.run {
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList, bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    override fun setArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            homeAdapter.run {
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

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        homeAdapter.setEnableLoadMore(false)
        mPresenter.requestBanner()
        mPresenter.requestArticles(0)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        swipeRefreshLayout.isRefreshing = false
        val page = homeAdapter.data.size / 20
        mPresenter.requestArticles(page)
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
     * BannerClickListener
     */
    private val bannerDelegate = BGABanner.Delegate<ImageView, String> { banner, imageView, model, position ->
        if (bannerDatas.size > 0) {
            val data = bannerDatas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.url)
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
                            if (isLogin) {
                                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                                    showSnackMsg(resources.getString(R.string.no_network))
                                    return@OnItemChildClickListener
                                }
                                val collect = data.collect
                                data.collect = !collect
                                homeAdapter.setData(position, data)
                                if (collect) {
                                    mPresenter.cancelCollectArticle(data.id)
                                } else {
                                    mPresenter.addCollectArticle(data.id)
                                }
                            } else {
                                Intent(activity, LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showToast(resources.getString(R.string.login_tint))
                            }
                        }
                    }
                }
            }

}