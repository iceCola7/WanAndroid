package com.cxz.wanandroid.ui.fragment

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
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner
import com.cxz.wanandroid.mvp.presenter.HomePresenter
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

    private val mPresenter: HomePresenter by lazy {
        HomePresenter()
    }

    private val datas = mutableListOf<Article>()

    private val bannerView by lazy {
        layoutInflater.inflate(R.layout.item_home_banner, null)
    }

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
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
            setAdapter(object : BGABanner.Adapter<ImageView, String> {
                override fun fillBannerItem(bgaBanner: BGABanner?, imageView: ImageView?, feedImageUrl: String?, position: Int) {
                    Glide.with(activity)
                            .load(feedImageUrl)
                            .transition(DrawableTransitionOptions().crossFade())
                            .into(imageView)
                }
            })
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