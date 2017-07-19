package com.lx.dailypush.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qihoo360.replugin.RePlugin
import android.content.Intent
import android.support.v7.app.AppCompatDelegate
import org.jetbrains.anko.doAsync
import java.io.Serializable


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        setContentView(setxml())
        init()
    }

open fun setxml() :Int{
    return 0
}

open fun run (){doAsync { dobynewthread() }}

open fun init(){

}

open fun dobynewthread(){
}
open fun startactivity(name : String,cls : String){
        RePlugin.startActivity(this@BaseActivity, RePlugin.createIntent(name, cls));
    }

open fun install(path: String){
        RePlugin.install(path)
    }

open fun uninstall(path: String){
        RePlugin.uninstall(path)
    }

open fun posttoplugin(obj :Serializable){
        //  通过Intent类的构造方法指定广播的ID
        val intent = Intent("hosttoplugin")
        //  将要广播的数据添加到Intent对象中
        intent.putExtra("object", obj)
        //  发送广播
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
