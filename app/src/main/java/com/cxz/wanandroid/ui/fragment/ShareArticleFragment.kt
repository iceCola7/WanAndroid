package com.cxz.wanandroid.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseMvpFragment
import com.cxz.wanandroid.mvp.contract.ShareArticleContract
import com.cxz.wanandroid.mvp.presenter.ShareArticlePresenter
import kotlinx.android.synthetic.main.fragment_share_article.*

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc 分享文章
 */
class ShareArticleFragment : BaseMvpFragment<ShareArticleContract.View, ShareArticlePresenter>(), ShareArticleContract.View {

    companion object {
        fun getInstance(): ShareArticleFragment = ShareArticleFragment()
    }

    override fun getArticleTitle(): String = et_article_title.text.toString().trim()

    override fun getArticleLink(): String = et_article_link.text.toString().trim()

    override fun createPresenter(): ShareArticlePresenter = ShareArticlePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_share_article

    override fun initView(view: View) {
        // 在fragment中使用 onCreateOptionsMenu 时需要在 onCrateView 中添加此方法，否则不会调用
        setHasOptionsMenu(true)
        super.initView(view)
    }

    override fun lazyLoad() {
    }

    override fun showShareArticle(success: Boolean) {
        if (success) {
            showDefaultMsg(getString(R.string.share_success))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_share_article, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share_article -> {
                mPresenter?.shareArticle()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}