package com.cxz.wanandroid.ui.fragment

import android.support.design.widget.TabLayout
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.ProjectPagerAdapter
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.mvp.contract.ProjectContract
import com.cxz.wanandroid.mvp.model.bean.ProjectTreeBean
import com.cxz.wanandroid.mvp.presenter.ProjectPresenter
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Created by chenxz on 2018/5/15.
 */
class ProjectFragment : BaseFragment(), ProjectContract.View {

    companion object {
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    private val mPresenter: ProjectPresenter by lazy {
        ProjectPresenter()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_project

    /**
     * ProjectTreeBean
     */
    private var projectTree = mutableListOf<ProjectTreeBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTree, fragmentManager)
    }

    override fun initView() {
        mPresenter.attachView(this)

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = projectTree.size
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }
    }

    override fun lazyLoad() {
        mPresenter.requestProjectTree()
    }

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            projectTree.addAll(it)
            viewPager.adapter = viewPagerAdapter
        }
    }

    override fun scrollToTop() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}