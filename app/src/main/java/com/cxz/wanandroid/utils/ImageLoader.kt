package com.cxz.wanandroid.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Created by chenxz on 2018/6/12.
 */
object ImageLoader {

    /**
     * 加载图片
     * @param context
     * @param url
     * @param iv
     */
    fun load(context: Context?, url: String?, iv: ImageView?) {
        if (!SettingUtil.getIsNoPhotoMode()) {
            iv?.let {
                Glide.with(context!!)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade())
                        .into(iv)
            }
        }
    }

}