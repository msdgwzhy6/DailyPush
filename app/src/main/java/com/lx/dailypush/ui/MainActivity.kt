package com.lx.dailypush.ui

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.droi.sdk.DroiError
import com.droi.sdk.core.*
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.droi.sdk.core.DroiQuery
import com.jingewenku.abrahamcaijin.commonutil.AppACache
import com.jingewenku.abrahamcaijin.commonutil.AppNetworkMgr
import com.lx.dailypush.R
import com.lx.dailypush.utils.HeaderImageLoader
import com.tencent.bugly.Bugly
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import org.jetbrains.anko.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {
val rv : RecyclerView by lazy {
    find<RecyclerView>(R.id.recyclerview)
}
    val navigation : BottomNavigationView by lazy {
        findViewById(R.id.navigation) as BottomNavigationView
    }
    val refreshLayout :RefreshLayout by lazy {
        findViewById(R.id.refreshLayout) as RefreshLayout
    }
    val mOnNavigationItemSelectedListener by lazy {
        OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_push -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_zhihu-> {
                    toast("正在启动知乎日报模块，请稍候")
                    doAsync {
                        startactivity("zhihu","com.lx.plugs.zhihu.MainActivity")
                    }

                    return@OnNavigationItemSelectedListener false
                }
                R.id.navigation_bbs -> {
                    toast("正在启动BBS模块，请稍候")
                    doAsync {
                        startactivity("bbs","com.lx.plugs.bbs.MainActivity")
                    }
                    return@OnNavigationItemSelectedListener false
                }
            }
            false
        }
    }
    val adapter :BaseQuickAdapter<DroiObject,BaseViewHolder> by lazy {
        object : BaseQuickAdapter<DroiObject,BaseViewHolder>(R.layout.item_main){
            override fun convert(helper: BaseViewHolder?, item: DroiObject?) {
                helper?.setText(R.id.title,item?.getString("title"))?.
                        setText(R.id.content,item?.getString("content"))?.
                        setText(R.id.dec,"来自 ${item?.getString("author")} 于 ${item?.modifiedTime.toString()}")
               var img= (item?.get("img") as DroiReferenceObject?)?.droiObject() as DroiFile?
                img?.getUriInBackground { uri, droiError ->
                    if (droiError.isOk){
                        Glide.with(this@MainActivity)
                                .load(uri)
                                .placeholder(R.mipmap.ic_launcher)
                                .crossFade()
                                .into(helper?.getView<ImageView>(R.id.img) as ImageView)
                    }
                    else{
                        Snackbar.make(rv,droiError.appendedMessage,Snackbar.LENGTH_SHORT).show()
                    }
                }
                helper?.setOnClickListener(R.id.cardView, View.OnClickListener {
                    startActivity((Intent(this@MainActivity, DetailActivity::class.java).putExtra("content",helper?.getView<TextView>(R.id.content).text).
                            putExtra("title",helper?.getView<TextView>(R.id.title).text)))
                })
            }

        }
    }
    val lm :LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    val q by lazy {
        DroiQuery.Builder.newBuilder().query("push").orderBy("_ModifiedTime",false).limit(10).offset(i!!).build()
    }
    val q_header by lazy {
        DroiQuery.Builder.newBuilder().query("dailypush_header").build()
    }
    var i :Int? = 0
    val header : View by lazy {
        layoutInflater.inflate(R.layout.view_header,null)
    }
    var  list : MutableList<String> = ArrayList()
    var list_title :MutableList<String> = ArrayList()
    val banner :Banner by lazy {
        header.find<Banner>(R.id.banner)
    }
    val emptyview : View  by lazy {
        layoutInflater.inflate(R.layout.view_empty,null)
    }
    private fun query_header(){
q_header.runQueryInBackground { mutableList :MutableList<DroiObject>, droiError ->
    if (droiError.isOk){
        mutableList.forEach { objects ->
           list.add(((objects.get("img") as DroiReferenceObject).droiObject() as DroiFile).uri.toString())
            list_title.add(objects.getString("title"))
        }
        banner.setImageLoader(HeaderImageLoader())
        banner.setImages(list)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
        banner.setBannerAnimation(Transformer.DepthPage)
        banner.setDelayTime(4000)
        banner.setBannerTitles(list_title)
        banner.start()
        adapter.addHeaderView(header)
    }
    else{
        toast(droiError.appendedMessage)
    }
}
    }
private fun query(){
    q.runQueryInBackground(object :DroiQueryCallback<DroiObject>{
        override fun result(p0: MutableList<DroiObject>?, p1: DroiError?) {
            if (p1?.isOk as Boolean){
                if(p0?.size==i){
                    Snackbar.make(rv,"暂无更多数据",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    i= p0?.size
                    adapter.setNewData(p0)
                }
                this@MainActivity.refreshLayout?.finishRefresh(2000)
                this@MainActivity.refreshLayout?.finishLoadmore(2000)
            }
            else{
                this@MainActivity.refreshLayout?.finishRefresh(2000)
                this@MainActivity.refreshLayout?.finishLoadmore(2000)
                Snackbar.make(rv,"获取数据失败，请重试",Snackbar.LENGTH_SHORT).show()
            }
        }
    })
}
    override fun init() {
        query()
        query_header()
        rv.layoutManager=lm
        rv.adapter=adapter
        emptyview.find<Button>(R.id.refresh).setOnClickListener {
            query()
            query_header()
        }
        adapter.emptyView=emptyview
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                query()
            }
        })
        refreshLayout.setOnLoadmoreListener { refreshlayout ->
            query()
        }
        run()
        Bugly.setAppChannel(this,"DailyPush")
        Bugly.init(getApplicationContext(), "279f49885e", false)
    }
    override fun dobynewthread() {
        isShowed()
        runOnUiThread {
            check_net()
        }
    }
    override fun setxml(): Int {
        return R.layout.activity_main
    }
    private fun isShowed() {
        when(AppACache.get(this@MainActivity).getAsString("isShowed").isNullOrEmpty()){
            true ->{
                AppACache.get(this@MainActivity).put("isShowed","true")
                startActivity<AboutActivity>()
            }
            false -> {

            }
        }
    }
    private fun check_net(){
        when(AppNetworkMgr.getCurrentNetworkType(this@MainActivity)){
            AppNetworkMgr.TYPE_WIFI ->{
                toast("网络状态正常")
            }
            AppNetworkMgr.TYPE_NO ->{
                toast("网络异常，请连接网络")
            }
        }
    }
    private fun check_user(){
        if (AppACache.get(this@MainActivity).getAsObject("login_result") == null) {

        }
        else{
            toast("验证通过")
        }
    }
}
