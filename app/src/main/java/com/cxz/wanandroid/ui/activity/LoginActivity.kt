package com.cxz.wanandroid.ui.activity

import android.content.Intent
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseActivity
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.LoginEvent
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.LoginContract
import com.cxz.wanandroid.mvp.model.bean.LoginData
import com.cxz.wanandroid.mvp.presenter.LoginPresenter
import com.cxz.wanandroid.utils.DialogUtil
import com.cxz.wanandroid.utils.Preference
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(), LoginContract.View {

    /**
     * local username
     */
    private var user: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * local password
     */
    private var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    private val mPresenter: LoginPresenter by lazy {
        LoginPresenter()
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this,getString(R.string.login_ing))
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun showError(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_login

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)
        et_username.setText(user)
        btn_login.setOnClickListener(onClickListener)
        tv_sign_up.setOnClickListener(onClickListener)
    }

    override fun start() {
    }

    override fun loginSuccess(data: LoginData) {
        showToast(getString(R.string.login_success))
        isLogin = true
        user = data.username
        pwd = data.password

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    override fun loginFail() {
    }

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_login -> {
                login()
            }
            R.id.tv_sign_up -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    /**
     * Login
     */
    private fun login() {

        if (validate()) {
            mPresenter.loginWanAndroid(et_username.text.toString(), et_password.text.toString())
        }

    }

    /**
     * Check UserName and PassWord
     */
    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()

        if (username.isEmpty()) {
            et_username.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            et_password.error = getString(R.string.password_not_empty)
            valid = false
        }
        return valid

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
