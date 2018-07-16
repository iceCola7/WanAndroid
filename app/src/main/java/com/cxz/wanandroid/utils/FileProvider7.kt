package com.cxz.wanandroid.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import java.io.File

/**
 * Created by chenxz on 2018/7/16.
 */
object FileProvider7 {

    fun getUriForFile(context: Context, file: File): Uri? {
        var fileUri: Uri? = null
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }


    fun getUriForFile24(context: Context, file: File): Uri {
        val fileUri = android.support.v4.content.FileProvider.getUriForFile(context,
                context.packageName + ".fileprovider",
                file)
        return fileUri
    }


    fun setIntentDataAndType(context: Context,
                             intent: Intent,
                             type: String,
                             file: File,
                             writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    fun setIntentData(context: Context,
                      intent: Intent,
                      file: File,
                      writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.data = getUriForFile(context, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.data = Uri.fromFile(file)
        }
    }

    fun grantPermissions(context: Context, intent: Intent, uri: Uri, writeAble: Boolean) {

        var flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (writeAble) {
            flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        intent.addFlags(flag)
        val resInfoList = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, flag)
        }
    }

}