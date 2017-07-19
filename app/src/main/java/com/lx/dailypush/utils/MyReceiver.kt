package com.lx.dailypush.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

/**
 * Created by Administrator on 2017/7/14 0014.
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        //  判断是否为sendbroadcast发送的广播
        if ("plugintohost".equals(p1?.action))
        {
            var bundle = p1?.extras;
            if (bundle != null)
            {
                Toast.makeText(p0,bundle.getString("object"), Toast.LENGTH_SHORT).show()
            }
        }
    }

}