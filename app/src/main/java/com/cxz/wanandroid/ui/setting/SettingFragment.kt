package com.cxz.wanandroid.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.AlertDialog
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.cxz.wanandroid.R
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showSnackMsg
import com.cxz.wanandroid.rx.SchedulerUtils
import com.cxz.wanandroid.ui.activity.CommonActivity
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.utils.CacheDataUtil
import com.cxz.wanandroid.widget.IconPreference
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit


/**
 * Created by chenxz on 2018/6/13.
 */
class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var context: SettingActivity? = null
    private lateinit var colorPreview: IconPreference

    companion object {
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_setting)
        setHasOptionsMenu(true)
        context = activity as SettingActivity

        colorPreview = findPreference("color") as IconPreference

        setDefaultText()

        findPreference("switch_show_top").setOnPreferenceChangeListener { preference, newValue ->
            // 通知首页刷新数据
            // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
            Observable.timer(100, TimeUnit.MILLISECONDS)
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe({
                        EventBus.getDefault().post(RefreshHomeEvent(true))
                    }, {})
            true
        }

        findPreference("auto_nightMode").setOnPreferenceClickListener {
            context?.startWithFragment(AutoNightModeFragment::class.java.name, null, null, 0, null)
            true
        }

        findPreference("color").setOnPreferenceClickListener {
            ColorChooserDialog.Builder(context!!, R.string.choose_theme_color)
                    .backButton(R.string.back)
                    .cancelButton(R.string.cancel)
                    .doneButton(R.string.done)
                    .customButton(R.string.custom)
                    .presetsButton(R.string.back)
                    .allowUserColorInputAlpha(false)
                    .show()
            false
        }

        findPreference("clearCache").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            CacheDataUtil.clearAllCache(context!!)
            context?.showSnackMsg(getString(R.string.clear_cache_successfully))
            setDefaultText()
            false
        }

        findPreference("scanQrCode").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            Intent(activity, CommonActivity::class.java).run {
                putExtra(Constant.TYPE_KEY, Constant.Type.SCAN_QR_CODE_TYPE_KEY)
                startActivity(this)
            }
            false
        }

        try {
            val version = context?.resources?.getString(R.string.current_version).toString()
                    .plus(context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName)
            findPreference("version").summary = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        findPreference("version").setOnPreferenceClickListener {
            Beta.checkUpgrade()
            false
        }

        findPreference("official_website").setOnPreferenceClickListener {
            // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.official_website_url))))
            ContentActivity.start(activity, getString(R.string.official_website_url))
            false
        }

        findPreference("about_us").setOnPreferenceClickListener {
            Intent(activity, CommonActivity::class.java).run {
                putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                startActivity(this)
            }
            false
        }

        findPreference("changelog").setOnPreferenceClickListener {
            // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.changelog_url))))
            ContentActivity.start(activity, getString(R.string.changelog_url))
            false
        }

        findPreference("sourceCode").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.source_code_url))))
            ContentActivity.start(activity, getString(R.string.source_code_url))
            false
        }

        findPreference("copyRight").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.copyright)
                    .setMessage(R.string.copyright_content)
                    .setCancelable(true)
                    .show()
            false
        }

    }

    private fun setDefaultText() {
        try {
            findPreference("clearCache").summary = CacheDataUtil.getTotalCacheSize(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (key == "color") {
            colorPreview.setView()
        }

    }
}