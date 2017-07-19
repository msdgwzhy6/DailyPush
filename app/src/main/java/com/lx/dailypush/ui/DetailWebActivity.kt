package com.lx.dailypush.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lx.dailypush.R
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class DetailWebActivity : AppCompatActivity() {
    val webview : WebView by lazy {
        find<WebView>(R.id.webView)
    }
    val mWebSettings : WebSettings by lazy {
        webview.settings
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_web)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        webview.settings.setDefaultTextEncodingName("GBK")
        webview.webViewClient= object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                toast("已经屏蔽点击")
                return true;
            }
        }
        supportActionBar?.title=intent.getStringExtra("title")
        webview.loadData(intent.getStringExtra("content"),"text/html; charset=UTF-8", null)
    }
}
