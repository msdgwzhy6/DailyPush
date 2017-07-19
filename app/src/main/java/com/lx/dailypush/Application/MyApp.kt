package com.lx.dailypush.Application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDex
import com.droi.sdk.analytics.DroiAnalytics
import com.droi.sdk.core.Core
import com.jingewenku.abrahamcaijin.commonutil.application.Utils
import com.lx.dailypush.BuildConfig
import com.qihoo360.replugin.*
import com.qihoo360.replugin.model.PluginInfo
import com.tencent.android.tpush.XGPush4Msdk
import com.tencent.android.tpush.XGPushManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast


/**
 * Created by Administrator on 2017/7/13 0013.
 */
class MyApp : RePluginApplication() {
    override fun createConfig(): RePluginConfig {
        val c = RePluginConfig()
        c.verifySign = !BuildConfig.DEBUG
        c.setCallbacks(object :RePluginCallbacks(this){
            override fun onPluginNotExistsForActivity(context: Context?, plugin: String?, intent: Intent?, process: Int): Boolean {
                //在插件不存在时，提示下载
                toast("该组件丢失")
                return super.onPluginNotExistsForActivity(context, plugin, intent, process)
            }
        })
        c.setEventCallbacks(object :RePluginEventCallbacks(this){
            override fun onInstallPluginFailed(path: String?, code: InstallResult?) {
                //加载插件失败
                toast("该组件故障")
                super.onInstallPluginFailed(path, code)
            }

            override fun onInstallPluginSucceed(info: PluginInfo?) {
                //加载插件成功
                toast("该插件加载成功")
                super.onInstallPluginSucceed(info)
            }

            override fun onStartActivityCompleted(plugin: String?, activity: String?, result: Boolean) {
                //启动activity完成
                super.onStartActivityCompleted(plugin, activity, result)
            }

            override fun onActivityDestroyed(activity: Activity?) {
                //activity被销毁
                super.onActivityDestroyed(activity)
            }
        })
        return c
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()
        doAsync {
            RePlugin.addCertSignature("C8FB19E57784F7200A94D947FF957D8D");
            Core.initialize(this@MyApp)
            DroiAnalytics.initialize(this@MyApp)
            DroiAnalytics.enableActivityLifecycleCallbacks(this@MyApp)
            Utils.init(this@MyApp)
            XGPushManager.setTag(this@MyApp,"DailyPush")
            XGPushManager.registerPush(this@MyApp)
        }
    }
}

