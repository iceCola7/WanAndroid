package com.cxz.wanandroid.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog

/**
 * Created by chenxz on 2018/6/9.
 * 对话框辅助类,需要自己调用show方法
 */
object DialogUtil {

    /**
     * 获取一个Dialog
     *
     * @param context
     * @return
     */
    fun getDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    /**
     * 获取一个耗时的对话框 ProgressDialog
     *
     * @param context
     * @param message
     * @return
     */
    fun getWaitDialog(context: Context, message: String): ProgressDialog {
        val waitDialog = ProgressDialog(context)
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message)
        }
        return waitDialog
    }

    /**
     * 获取一个信息对话框,注意需要自己手动调用show方法
     *
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    @JvmOverloads
    fun getMessageDialog(
        context: Context, message: String,
        onClickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onClickListener)
        return builder
    }

    fun getConfirmDialog(
        context: Context, message: String,
        onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onClickListener)
        builder.setNegativeButton("取消", null)
        return builder
    }

    fun getConfirmDialog(
        context: Context,
        message: String,
        onOKClickListener: DialogInterface.OnClickListener,
        onCancelClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onOKClickListener)
        builder.setNegativeButton("取消", onCancelClickListener)
        return builder
    }

    fun getSelectDialog(
        context: Context, title: String, arrays: Array<String>,
        onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setItems(arrays, onClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setNegativeButton("取消", null)
        return builder
    }

    fun getSelectDialog(
        context: Context, arrays: Array<String>,
        onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        return getSelectDialog(context, "", arrays, onClickListener)
    }

    /**
     * 获取一个单选的对话框
     *
     * @param context
     * @param title
     * @param arrays
     * @param selectIndex
     * @param onClickListener
     * @param onOKClickListener
     * 点击确定的回调接口
     * @param onCancelClickListener
     * 点击取消的回调接口
     * @return
     */
    fun getSingleChoiceDialog(
        context: Context,
        title: String,
        arrays: Array<String>,
        selectIndex: Int,
        onClickListener: DialogInterface.OnClickListener,
        onOKClickListener: DialogInterface.OnClickListener,
        onCancelClickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setPositiveButton("确定", onOKClickListener)
        builder.setNegativeButton("取消", onCancelClickListener)
        return builder
    }

    fun getSingleChoiceDialog(
        context: Context, title: String, arrays: Array<String>,
        selectIndex: Int, onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setPositiveButton("取消", null)
        return builder
    }

    fun getSingleChoiceDialog(
        context: Context, arrays: Array<String>, selectIndex: Int,
        onClickListener: DialogInterface.OnClickListener, onOKClickListener: DialogInterface.OnClickListener,
        onCancelClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        return getSingleChoiceDialog(
            context, "", arrays, selectIndex, onClickListener, onOKClickListener,
            onCancelClickListener
        )
    }

    /**
     * 获取一个多选的对话框
     *
     * @param context
     * @param title
     * @param arrays
     * @param checkedItems
     * @param onMultiChoiceClickListener
     * @param onOKClickListener
     * 点击确定的回调接口
     * @param onCancleListener
     * 点击取消的回调接口
     * @return
     */
    fun getMultiChoiceDialog(
        context: Context,
        title: String,
        arrays: Array<String>,
        checkedItems: BooleanArray,
        onMultiChoiceClickListener: DialogInterface.OnMultiChoiceClickListener,
        onOKClickListener: DialogInterface.OnClickListener,
        onCancelClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMultiChoiceItems(arrays, checkedItems, onMultiChoiceClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setPositiveButton("确定", onOKClickListener)
        builder.setNegativeButton("取消", onCancelClickListener)
        return builder
    }

}

