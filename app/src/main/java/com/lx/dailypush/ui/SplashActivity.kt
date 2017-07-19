package com.lx.dailypush.ui

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.droi.sdk.core.DroiFile
import com.droi.sdk.core.DroiObject
import com.droi.sdk.core.DroiQuery
import com.droi.sdk.core.DroiReferenceObject
import com.lx.dailypush.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


/**
 * Created by Administrator on 2017/7/13 0013.
 */
class SplashActivity : Activity() {
    val q_splash by lazy {
        DroiQuery.Builder.newBuilder().query("splash").build()
    }
    val imgview : ImageView by lazy {
        find<ImageView>(R.id.splash_img)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        query_splash()
    }
    private fun query_splash() {
        q_splash.runQueryInBackground { mutableList: MutableList<DroiObject>, droiError ->
            if (droiError.isOk) {
                mutableList.forEach { objects ->
                    Glide.with(this@SplashActivity)
                            .load(((objects.get("img") as DroiReferenceObject).droiObject() as DroiFile).uri.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .crossFade()
                            .fitCenter()
                            .into(imgview)
                    doAsync {
                        Thread.sleep(5000)
                        runOnUiThread {
                            this@SplashActivity.finish()
                            startActivity<MainActivity>()
                        }
                    }
                }
            } else {
                doAsync {
                    Thread.sleep(2000)
                    runOnUiThread {
                        this@SplashActivity.finish()
                        startActivity<MainActivity>()
                    }
                }
            }
        }
    }
}