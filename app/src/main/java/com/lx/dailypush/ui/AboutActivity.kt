package com.lx.dailypush.ui

import android.support.design.widget.Snackbar
import android.widget.ImageView
import com.lx.dailypush.R
import org.jetbrains.anko.find

/**
 * Created by Administrator on 2017/7/15 0015.
 */
class AboutActivity : BaseActivity() {
    val img by lazy {
        find<ImageView>(R.id.about_img)
    }
    override fun setxml(): Int {
        return R.layout.activity_about
    }

    override fun init() {
        Snackbar.make(img,"这里是作者有话说界面，按返回键退出",Snackbar.LENGTH_INDEFINITE).show()
    }
}