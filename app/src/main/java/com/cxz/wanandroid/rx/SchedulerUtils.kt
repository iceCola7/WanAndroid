package com.cxz.wanandroid.rx

import com.cxz.wanandroid.rx.scheduler.IoMainScheduler

/**
 * Created by chenxz on 2018/4/21.
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}