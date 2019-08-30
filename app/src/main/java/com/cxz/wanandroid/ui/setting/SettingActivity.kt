package com.cxz.wanandroid.ui.setting

import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseSwipeBackActivity
import com.cxz.wanandroid.event.ColorEvent
import com.cxz.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class SettingActivity : BaseSwipeBackActivity(), ColorChooserDialog.ColorCallback {

    private val EXTRA_SHOW_FRAGMENT = "show_fragment"
    private val EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args"
    private val EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title"

    override fun attachLayoutRes(): Int = R.layout.activity_setting

    override fun initData() {
    }

    override fun initView() {
        val initFragment: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT) ?: ""
        val initArguments: Bundle = intent.getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS) ?: Bundle()
        val initTitle: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE)
                ?: resources.getString(R.string.setting)

        if (initFragment.isEmpty()) {
            setupFragment(SettingFragment::class.java.name, initArguments)
        } else {
            setupFragment(initFragment, initArguments)
        }

        toolbar.run {
            title = initTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun start() {
    }

    private fun setupFragment(fragmentName: String, args: Bundle) {
        val fragment = Fragment.instantiate(this, fragmentName, args)
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun onBuildStartFragmentIntent(fragmentName: String, args: Bundle?, title: String?): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this, javaClass)
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, title)
        return intent
    }

    fun startWithFragment(fragmentName: String, args: Bundle?,
                          resultTo: Fragment?, resultRequestCode: Int, title: String?) {
        val intent = onBuildStartFragmentIntent(fragmentName, args, title)
        if (resultTo == null) {
            startActivity(intent)
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode)
        }
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            SettingUtil.setColor(selectedColor)
        }
        initColor()
        EventBus.getDefault().post(ColorEvent(true))
    }

}
