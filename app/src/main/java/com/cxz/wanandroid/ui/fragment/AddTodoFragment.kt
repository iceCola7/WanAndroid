package com.cxz.wanandroid.ui.fragment

import android.os.Bundle
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.ext.formatCurrentDate
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.bean.TodoBean
import com.cxz.wanandroid.mvp.presenter.AddTodoPresenter
import kotlinx.android.synthetic.main.fragment_add_todo.*
import java.util.*

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoFragment : BaseFragment(), AddTodoContract.View {

    companion object {
        fun getInstance(bundle: Bundle): AddTodoFragment {
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy {
        AddTodoPresenter()
    }

    /**
     * Date
     */
    private var mCurrentDate = formatCurrentDate()

    /**
     * 类型
     */
    private var mType: Int = 0
    private var mTodoBean: TodoBean? = null
    /**
     * 新增，编辑，查看 三种状态
     */
    private var mTypeKey = ""

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_add_todo

    override fun getType(): Int = mType
    override fun getCurrentDate(): String = mCurrentDate
    override fun getTitle(): String = et_title.text.toString()
    override fun getContent(): String = et_content.text.toString()

    override fun initView() {
        mPresenter.attachView(this)
        tv_date.text = mCurrentDate

        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
        mTypeKey = arguments?.getString(Constant.TYPE_KEY) ?: Constant.Type.ADD_TODO_TYPE_KEY

        when (mTypeKey) {
            Constant.Type.ADD_TODO_TYPE_KEY -> {

            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
                et_title.isEnabled = false
                et_content.isEnabled = false
                ll_date.isEnabled = false
                btn_save.visibility = View.GONE
            }
        }

        ll_date.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = android.app.DatePickerDialog(
                    activity,
                    android.app.DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val currentMonth = month + 1
                        mCurrentDate = "$year-$currentMonth-$dayOfMonth"
                        tv_date.text = mCurrentDate
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        btn_save.setOnClickListener {
            when (mTypeKey) {
                Constant.Type.ADD_TODO_TYPE_KEY -> {
                    mPresenter.addTodo()
                }
                Constant.Type.EDIT_TODO_TYPE_KEY -> {

                }
            }
        }

    }

    override fun lazyLoad() {
    }

    override fun showAddTodo(success: Boolean) {
        if (success) {
            showToast(getString(R.string.save_success))
            activity?.finish()
        }
    }

    override fun showUpdateTodo(success: Boolean) {
        if (success) {
            showToast(getString(R.string.save_success))
            activity?.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}