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

        //?????????????????????,??????shouldOverrideUrlLoading????????????true,????????????????????????????????????,?????????????????????????????????
        //????????????true????????????????????????
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
                    //wx.tenpay.com ???????????????????????????shouldOverrideUrlLoading????????????????????????????????????
                    headers["Referer"] = HTTP_URL //?????????????????????????????? ?????????????????????
                } else {
                    //payh5.bbnpay
                    if (!isFirstLoad) {
                        //??????????????????
                        headers["Referer"] = HTTP_URL //????????????H5????????????????????????
                        isFirstLoad = true
                    } else {
                        //???????????????????????????shouldOverrideUrlLoading????????????????????????????????????
                        headers["Referer"] = HTTP_URL //?????????????????????????????? ?????????????????????
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
     * ??????scheme ???????????????????????????????????????????????????????????????????????????webView ??????
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
                    // ?????????????????????
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
        LogUtils.d("loadUrl>>webview??????url>>$linkUrl")

//        mBinding.webview.loadUrl(linkUrl, headers);
        if ("4.4.3" == Build.VERSION.RELEASE
            || "4.4.4" == Build.VERSION.RELEASE
        ) {
            //???????????????????????????referer???????????????
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
            //???????????????????????????????????????????????????TextView?????????
            override fun onProgressChanged(webView: WebView, progress: Int) {
                super.onProgressChanged(webView, progress)
                if (progress == 100) {
                    mBinding!!.lineProgress.visibility = View.GONE //??????????????????????????????
                } else {
                    mBinding!!.lineProgress.visibility = View.VISIBLE //????????????????????????????????????
                    mBinding!!.lineProgress.progress = progress //???????????????
                }
            }

            //??????Web?????????title??????????????????????????????title
            //???????????????????????????????????????????????????onReceiveTitle????????????????????? ??????????????????,
            //?????????????????????onReceiveError??????????????????????????????title
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
                builder.setTitle("?????????")
                    .setMessage(message)
                    .setPositiveButton("??????", null)
                builder.setOnKeyListener { dialog, keyCode, event ->
                    Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event)
                    true
                }
                // ???????????????back????????????
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                result.confirm() // ???????????????????????????????????????confirm,??????????????????????????????????????????
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
        private const val BAR_TITLE = "??????????????????"
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