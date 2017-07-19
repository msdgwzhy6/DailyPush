package com.lx.dailypush.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lx.dailypush.R
import com.youth.banner.loader.ImageLoader

/**
 * Created by Administrator on 2017/7/17 0017.
 */
class HeaderImageLoader: ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .fitCenter()
                .into(imageView)
    }
}