package com.cxz.wanandroid.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.cxz.wanandroid.R
import com.cxz.wanandroid.base.BaseMvpFragment
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshTodoEvent
import com.cxz.wanandroid.ext.formatCurrentDate
import com.cxz.wanandroid.ext.showToast
import com.cxz.wanandroid.ext.stringToCalendar
import com.cxz.wanandroid.mvp.contract.AddTodoContract
import com.cxz.wanandroid.mvp.model.bean.TodoBean
import com.cxz.wanandroid.mvp.presenter.AddTodoPresenter
import com.cxz.wanandroid.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_add_todo.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoFragment : BaseMvpFragment<AddTodoContract.View, AddTodoContract.Presenter>(), AddTodoContract.View {

    companion object {
        fun getInstance(bundle: Bundle): AddTodoFragment {
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): AddTodoContract.Presenter = AddTodoPresenter()

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
    /**
     * id
     */
    private var mId: Int? = 0

    /**
     * 优先级  重要（1），一般（0）
     */
    private var mPriority = 0

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(activity!!, getString(R.string.save_ing))
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_add_todo

    override fun getType(): Int = mType
    override fun getCurrentDate(): String = tv_date.text.toString()
    override fun getTitle(): String = et_title.text.toString()
    override fun getContent(): String = et_content.text.toString()
    override fun getStatus(): Int = mTodoBean?.status ?: 0
    override fun getItemId(): Int = mTodoBean?.id ?: 0
    override fun getPriority(): String = mPriority.toString()

    override fun initView(view: View) {
        super.initView(view)

        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
        mTypeKey = arguments?.getString(Constant.TYPE_KEY) ?: Constant.Type.ADD_TODO_TYPE_KEY

        when (mTypeKey) {
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                tv_date.text = formatCurrentDate()
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
                mPriority = mTodoBean?.priority ?: 0
                if (mTodoBean?.priority == 0) {
                    rb0.isChecked = true
                    rb1.isChecked = false
                } else if (mTodoBean?.priority == 1) {
                    rb0.isChecked = false
                    rb1.isChecked = true
                }
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
                iv_arrow_right.visibility = View.GONE

                ll_priority.isEnabled = false
                if (mTodoBean?.priority == 0) {
                    rb0.isChecked = true
                    rb1.isChecked = false
                    rb1.visibility = View.GONE
                } else if (mTodoBean?.priority == 1) {
                    rb0.isChecked = false
                    rb1.isChecked = true
                    rb0.visibility = View.GONE
                } else {
                    ll_priority.visibility = View.GONE
                }
            }
        }

        ll_date.setOnClickListener {
            var now = Calendar.getInstance()
            if (mTypeKey == Constant.Type.EDIT_TODO_TYPE_KEY) {
                mTodoBean?.dateStr?.let {
                    now = it.stringToCalendar()
                }
            }
            val dpd = android.app.DatePickerDialog(activity,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        mCurrentDate = "$year-${month + 1}-$dayOfMonth"
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
                    mPresenter?.addTodo()
                }
                Constant.Type.EDIT_TODO_TYPE_KEY -> {
                    mPresenter?.updateTodo(getItemId())
                }
            }
        }

        rg_priority.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb0) {
                mPriority = 0
                rb0.isChecked = true
                rb1.isChecked = false
            } else if (checkedId == R.id.rb1) {
                mPriority = 1
                rb0.isChecked = false
                rb1.isChecked = true
            }
        }

    }

    override fun lazyLoad() {
    }

    override fun showAddTodo(success: Boolean) {
        if (success) {
            showToast(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, mType))
            activity?.finish()
        }
    }

    override fun showUpdateTodo(success: Boolean) {
        if (success) {
            showToast(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, mType))
            activity?.finish()
        }
    }

}