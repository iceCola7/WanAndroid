package com.cxz.wanandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cxz.wanandroid.event.NetworkChangeEvent
import com.cxz.wanandroid.ext.loge
import com.cxz.wanandroid.utils.NetWorkUtil
import org.greenrobot.eventbus.EventBus

/**
 * Created by chenxz on 2018/8/1.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        loge("onReceive：当前网络状态------>>$isConnected")
        EventBus.getDefault().post(NetworkChangeEvent(isConnected))
    }

}