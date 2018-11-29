package com.cxz.wanandroid.event

import com.cxz.wanandroid.utils.SettingUtil

/**
 * Created by chenxz on 2018/6/18.
 */
class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())