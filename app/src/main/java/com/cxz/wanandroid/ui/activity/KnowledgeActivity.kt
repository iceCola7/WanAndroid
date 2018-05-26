package com.cxz.wanandroid.ui.activity

import android.content.Intent
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.cxz.wanandroid.R
import com.cxz.wanandroid.adapter.KnowledgePagerAdapter
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.common.Contanst
import com.cxz.wanandroid.mvp.model.bean.Knowledge
import com.cxz.wanandroid.mvp.model.bean.KnowledgeTreeBody
import kotlinx.android.synthetic.main.activity_knowledge.*
import kotlinx.android.synthetic.main.toolbar.*

class KnowledgeActivity : BaseActivity() {

    /**
     * datas
     */
    private var knowledges = mutableListOf<Knowledge>()

    /**
     * toolbar title
     */
    private lateinit var toolbarTitle: String

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: KnowledgePagerAdapter by lazy {
        KnowledgePagerAdapter(knowledges, supportFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_knowledge

    override fun initData() {
        intent.extras.let {
            toolbarTitle = it.getString(Contanst.CONTENT_TITLE_KEY)
            it.getSerializable(Contanst.CONTENT_DATA_KEY).let {
                val data = it as KnowledgeTreeBody
                data.children.let { children ->
                    knowledges.addAll(children)
                }
            }
        }
    }

    override fun initView() {
        toolbar.run {
            title = toolbarTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //StatusBarUtil.setPaddingSmart(this@KnowledgeActivity, toolbar)
        }
        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = knowledges.size
        }
        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }
    }

    override fun start() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_type_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                            getString(
                                    R.string.share_article_url,
                                    getString(R.string.app_name),
                                    knowledges[tabLayout.selectedTabPosition].name,
                                    knowledges[tabLayout.selectedTabPosition].id.toString()
                            ))
                    type = Contanst.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
