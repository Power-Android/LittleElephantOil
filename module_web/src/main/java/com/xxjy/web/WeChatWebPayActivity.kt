package com.xxjy.web

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.Constants
import com.xxjy.common.router.RouteConstants
import com.xxjy.web.databinding.ActivityWeChatWebPayBinding
import com.xxjy.web.jscalljava.JsOperation
import com.xxjy.web.viewmodel.WebViewViewModel
import java.net.URISyntaxException
import java.net.URLDecoder
import java.util.*

@Route(path = RouteConstants.Web.A_WECHAT_WEB_PAY)
class WeChatWebPayActivity : BindingActivity<ActivityWeChatWebPayBinding, WebViewViewModel>() {
    private var mPayLoadJs: String? = null
    private var instence: WeChatWebPayActivity? = null
    private var isFirstLoad = false
    private var isFirstUse = true
    private var isFirstWriteJs = true
    private val HTTP_URL = Constants.HTTP_CALL_BACK_URL

    @Autowired(name = "payJs")
    @JvmField
    var resultJs = ""


    override fun initView() {
        instence = this
        //        StatusBarUtil.setHeightAndPadding(this, mBinding.toolbar);
//        UiUtils.initWebView(mBinding!!.webview)
        val intent = intent
        if (TextUtils.isEmpty(resultJs)) {
            resultJs = intent.getStringExtra(TAG_PAY_LOAD_JS).toString()
        }

        //        mPayLoadJs = intent.getStringExtra(TAG_PAY_LOAD_JS);
        val first = "<script type=\"text/javascript\">location.href=\""
        val second = "\"</script>"
        mPayLoadJs = first + resultJs + second
        mBinding!!.webview.addJavascriptInterface(JsOperation(this), JsOperation.JS_USE_NAME)
        initWebChromeClient()
        initWebViewClient()
        LogUtils.d(mPayLoadJs)

        //该方法可以使用,但是shouldOverrideUrlLoading需要返回true,否则会出现不显示支付信息,商家参数格式有误的情况
        //使用返回true的方式推荐本方法
        mBinding!!.webview.loadUrl("file:///android_asset/weixin.html")
    }

    override fun initListener() {
        mBinding!!.webBack.setOnClickListener { view: View? -> onViewClicked(view) }
    }

    override fun onViewClicked(view: View?) {
        when (view!!.id) {
            R.id.web_back -> finish()
        }
    }

    override fun dataObservable() {}
    override fun onResume() {
        super.onResume()
        mBinding!!.webview.onResume()
        if (!isFirstUse) {
            finish()
        } else {
            isFirstUse = false
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding!!.webview.onPause()
    }

    private fun initWebViewClient() {
        mBinding!!.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (isFirstWriteJs) {
                    isFirstWriteJs = false
                    mBinding!!.webview.loadUrl("javascript:document.write('$mPayLoadJs');")
                    //                    mBinding.webview.loadUrl("javascript:" + mPayLoadJs);
                }
            }

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                if (url.endsWith("login/") || url.endsWith("login") || url.endsWith("loginPrev/") || url.endsWith(
                        "loginPrev"
                    )
                ) {
//                    UiUtils.toLoginActivity(this@WeChatWebPayActivity, 1)
                    return true
                }
                val headers: MutableMap<String, String> = HashMap()
                if (url.contains("wx.tenpay.com")) {
                    //wx.tenpay.com 收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第二次
                    headers["Referer"] = HTTP_URL //第三方支付平台请求头 一般是对方固定
                } else {
                    //payh5.bbnpay
                    if (!isFirstLoad) {
                        //跳转到收银台
                        headers["Referer"] = HTTP_URL //商户申请H5时提交的授权域名
                        isFirstLoad = true
                    } else {
                        //收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第一次
                        headers["Referer"] = HTTP_URL //第三方支付平台请求头 一般是对方固定
                    }
                }
                LogUtils.d("shouldOverrideUrlLoading>>$url")
                doSchemeJump(url, headers)
                return true
                //                return super.shouldOverrideUrlLoading(webView, url);
            }
        }
    }

    /**
     * 根据scheme 协议作出响应跳转是跳系统浏览器还是应用内页面还是用webView 打开
     */
    fun doSchemeJump(linkUrl: String, headers: Map<String, String>) {
        try {
            LogUtils.d("doSchemeJump>>$linkUrl")
            if (!TextUtils.isEmpty(linkUrl)) {
                val uri = Uri.parse(linkUrl)
                val scheme = uri.scheme
                if (scheme == "http" || scheme == "https") {
                    LogUtils.d("doSchemeJump>>>loadUrl>>$linkUrl")
                    loadUrl(linkUrl, uri, headers)
                } else {
                    // 调用系统浏览器
                    LogUtils.d("doSchemeJump>>>loadUrl>>$linkUrl")
                    openBrowser(linkUrl)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadUrl(linkUrl: String, uri: Uri, headers: Map<String, String>) {
        val bundle = parseExtras(uri)
        if (bundle != null) {
            if (bundle.containsKey("scheme")) {
                val scheme = bundle.getString("scheme")
                if (scheme != null && scheme.startsWith("alipays")) {
                    val schemeUrl = URLDecoder.decode(scheme)
                    try {
                        LogUtils.d("loadUrl>>open>>$linkUrl")
                        open(schemeUrl)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        openBrowser(linkUrl)
                        finish()
                    }
                    return
                }
            }
        }
        LogUtils.d("loadUrl>>webview加载url>>$linkUrl")

//        mBinding.webview.loadUrl(linkUrl, headers);
        if ("4.4.3" == Build.VERSION.RELEASE
            || "4.4.4" == Build.VERSION.RELEASE
        ) {
            //兼容这两个版本设置referer无效的问题
            mBinding!!.webview.loadDataWithBaseURL(
                HTTP_URL,
                "<script>window.location.href=\"$linkUrl\";</script>",
                "text/html", "utf-8", null
            )
        } else {
            val extraHeaders: MutableMap<String, String> = HashMap()
            extraHeaders["Referer"] = HTTP_URL
            mBinding!!.webview.loadUrl(linkUrl, extraHeaders)
        }
    }

    private fun openBrowser(url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(URISyntaxException::class)
    private fun open(url: String) {
        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
        intent.addCategory("android.intent.category.BROWSABLE")
        intent.component = null
        startActivity(intent)
    }

    private fun initWebChromeClient() {
        mBinding!!.webview.webChromeClient = object : WebChromeClient() {
            //
            //获得网页的加载进度，显示在右上角的TextView控件中
            override fun onProgressChanged(webView: WebView, progress: Int) {
                super.onProgressChanged(webView, progress)
                if (progress == 100) {
                    mBinding!!.lineProgress.visibility = View.GONE //加载完网页进度条消失
                } else {
                    mBinding!!.lineProgress.visibility = View.VISIBLE //开始加载网页时显示进度条
                    mBinding!!.lineProgress.progress = progress //设置进度值
                }
            }

            //获取Web页中的title用来设置自己界面中的title
            //当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为 找不到该网页,
            //因此建议当触发onReceiveError时，不要使用获取到的title
            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                mBinding!!.webPayTitle.text = s
            }

            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                val builder = AlertDialog.Builder(instence)
                builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                builder.setOnKeyListener { dialog, keyCode, event ->
                    Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event)
                    true
                }
                // 禁止响应按back键的事件
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                result.confirm() // 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        try {
            if (mBinding!!.webview != null) {
                if (null != mBinding!!.webview.parent) {
                    (mBinding!!.webview.parent as ViewGroup).removeView(mBinding!!.webview)
                }
                mBinding!!.webview.removeAllViews()
                mBinding!!.webview.destroy()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    companion object {
        private const val BAR_TITLE = "现代支付界面"
        private const val TAG_PAY_LOAD_JS = "payJs"
        fun parseExtras(uri: Uri): Bundle? {
            var extras: Bundle? = null
            val queryParameterNames = uri.queryParameterNames
            for (key in queryParameterNames) {
                val value = uri.getQueryParameter(key)
                if (extras == null) {
                    extras = Bundle()
                }
                extras.putString(key, value)
            }
            return extras
        }

        fun openWebPayAct(activity: Activity, loadJs: String?) {
            val intent = Intent(activity, WeChatWebPayActivity::class.java)
            if (!TextUtils.isEmpty(loadJs)) {
                intent.putExtra(TAG_PAY_LOAD_JS, loadJs)
            }
            activity.startActivity(intent)
        }
    }
}