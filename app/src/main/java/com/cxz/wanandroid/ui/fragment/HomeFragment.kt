package com.cxz.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.HomeAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner
import com.cxz.wanandroid.mvp.presenter.HomePresenter
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.widget.SpaceItemDecoration
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
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
            Glide.with(activity)
                    .load(feedImageUrl)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(imageView)
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
        mPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(true)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
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
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        homeAdapter.run {
            loadMoreComplete()
        }
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
                replaceData(it)
            }
        }
    }

    override fun setMoreArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            homeAdapter.run {
                addData(it)
            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        mPresenter.requestArticles(0)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = homeAdapter.data.size / 20 + 1
        mPresenter.requestArticles(page)
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
     * BannerClickListener
     */
    private val bannerDelegate = BGABanner.Delegate<ImageView, String> { banner, imageView, model, position ->
        if (bannerDatas.size > 0) {
            val data = bannerDatas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Contanst.CONTENT_URL_KEY, data.url)
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

}