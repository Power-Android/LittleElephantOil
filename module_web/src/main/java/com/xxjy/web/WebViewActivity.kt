package com.xxjy.web

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.*
import com.tencent.smtt.export.external.interfaces.JsPromptResult
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.entity.SharedInfoBean
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.util.StringUtils
import com.xxjy.push.JPushManager
import com.xxjy.umeng.SharedInfoManager.OnSharedAllResultListener
import com.xxjy.web.viewmodel.WebViewViewModel
import com.xxjy.web.databinding.ActivityWebviewBinding
import com.xxjy.web.jscalljava.JsOperation
import com.xxjy.web.jscalljava.jsbean.ToolBarStateBean
import com.xxjy.web.jscalljava.jscallback.OnJsCallListener
@Route(path = RouteConstants.Web.A_WEB)
class WebViewActivity : BindingActivity<ActivityWebviewBinding, WebViewViewModel>(), View.OnClickListener, OnJsCallListener {
    private lateinit var mToolbar: Toolbar
    private lateinit var mWebTitle: TextView
    private lateinit var mWebShared: ImageView
    private lateinit var mWebCallHelp: ImageView
    private lateinit var mWebBack: ImageView
    private lateinit var mWebClose: ImageView
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var INSTENCE: WebViewActivity
    private var isShared = false //?????????????????????????????????
    private var shared //???????????????
            : SharedInfoBean? = null
    private lateinit var mJsOperation: JsOperation
    @Autowired
    lateinit var url: String
    var isShouldLoadUrl = false
    private var mCallPhoneNumber = ""
    private  var mLastShowTitle:String =""
    private lateinit var mOrderNo: String
    private val mIsOpenVip = false
    private var defaultToolBarBgColor = 0
    private var defaultTitleColor //????????????????????????title??????
            = 0
    private var defaultTitleStateColor //?????????????????????title??????
            = 0
    private var isShouldAutoOpenWeb = false //???????????????????????????????????????????????????

    //??????????????????????????????
    private val shouldShowSureDialog = false
     private fun initData() {
        INSTENCE = this
//        StatusBarUtil.setHeightAndPadding(this, mToolbar)
        BarUtils.addMarginTopEqualStatusBarHeight(mToolbar!!)
//        url = intent.getStringExtra("url")
         LogUtils.d("url=:$url")
        initListener()
        mJsOperation = JsOperation(this)
        mJsOperation.setOnJsCallListener(this)
        initWebViewSettings()
        initPayWebViewSettings()
        initSharedInfo(url!!)
        initDefaultColor()
        webView!!.loadUrl(url)

    }

    private fun initDefaultColor() {
        defaultToolBarBgColor = Color.parseColor("#F9F9F9")
        defaultTitleColor = Color.parseColor("#232323")
        defaultTitleStateColor = Color.WHITE
    }

    //    @Override
    //    protected String getPageTitle() {
    //        return BAR_TITLE;
    //    }
    override fun initView() {
        webView = mBinding.webview
        mToolbar = mBinding.toolbar
        mWebTitle = mBinding.webTitle
        mWebShared = mBinding.webShared
        mWebCallHelp = mBinding.webHelp
        mWebBack = mBinding.webBack
        mWebClose = mBinding.webClose
        PermissionUtils.permission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    initData()
                }

                override fun onDenied() {
                }
            })
            .request()

    }

    override fun initListener() {
        mWebShared!!.setOnClickListener(this)
        mWebCallHelp!!.setOnClickListener(this)
        mWebBack!!.setOnClickListener(this)
        mWebClose!!.setOnClickListener(this)
    }

    override fun onViewClicked(view: View?) {}
    override fun dataObservable() {}
    override fun onStart() {
        super.onStart()
        if (isShouldLoadUrl) {
            isShouldLoadUrl = false
            if (UserConstants.login_status) {
                webView!!.reload()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.webview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.webview.onPause()
    }

    private fun initSharedInfo(url: String) {
        if (TextUtils.isEmpty(url)) return
        val aesInfo: String? = StringUtils.getUrlDate(url, "nativeShare")
        var sharedInfo: String? = null
        if (!TextUtils.isEmpty(aesInfo)) {
            sharedInfo = StringUtils.decodePwd(aesInfo)
        }
        if (TextUtils.isEmpty(sharedInfo)) {
            hideSharedIcon()
        } else {
            showSharedIcon(sharedInfo!!)
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param phoneNumber
     */
    fun showHelpIcon(phoneNumber: String) {
        mCallPhoneNumber = phoneNumber
        mWebCallHelp!!.visibility = View.VISIBLE
    }

    /**
     * ???????????????????????????????????????
     *
     * @param shardInfo
     */
    fun showSharedIcon(shardInfo: String) {
        isShared = true
        try {
            shared = GsonUtils.fromJson(shardInfo, SharedInfoBean::class.java)
        } catch (e: Exception) {
        }
        if (shared != null) {
            mWebShared!!.visibility = View.VISIBLE
        }
    }

    /**
     * ???????????????????????????????????????
     */
    fun hideSharedIcon() {
        isShared = false
        mWebShared.visibility = View.GONE
    }

    /**
     * ?????????????????????
     *
     * @param intentInfo
     */
    fun startAliPay(intentInfo: String?) {
//        try {
//            PayOrderResponse.DataBean orderResponse = GsonTool.parseJson(intentInfo, PayOrderResponse.DataBean.class);
//            if (orderResponse != null) {
//                disPathPayOrderInfo(orderResponse);
//            }
//        } catch (Exception e) {
//            showToastWarning("????????????");
//        }
    }

    /**
     * ?????????????????????
     *
     * @param intentInfo
     */
    fun startUnionPay(intentInfo: String?) {
//        try {
//            PayOrderResponse.DataBean orderResponse = GsonTool.parseJson(intentInfo, PayOrderResponse.DataBean.class);
//            if (orderResponse != null) {
//
//                disPathPayOrderInfo(orderResponse);
//            }
//        } catch (Exception e) {
//            showToastWarning("????????????");
//        }
    }

    /**
     * ??????toolbar?????????
     *
     * @param isDefault ???????????????????????????
     * @param bgColor   ?????????, isDefault=false ???????????????
     */
    fun changeToolBarState(isDefault: Boolean, bgColor: String) {
        try {
            if (isDefault) {
                mBinding.toolbar.setBackgroundColor(defaultToolBarBgColor)
                //            mBinding.webBack.setImageTintList(ColorStateList.valueOf(defaultBackTintColor));
//            mBinding.webClose.setImageTintList(ColorStateList.valueOf(defaultBackTintColor));
                mBinding.webBack.imageTintList = null
                mBinding.webClose.imageTintList = null
                mBinding.webHelp.imageTintList = null
                mBinding.webShared.imageTintList = null
                mBinding.webTitle.setTextColor(defaultTitleColor)
            } else {
                if (TextUtils.isEmpty(bgColor)) {
                    return
                }
                try {
                    var toolBarStateBean: ToolBarStateBean? = null
                    if (bgColor.startsWith("#")) {
                        toolBarStateBean = ToolBarStateBean()
                        toolBarStateBean.toolBarBgColor=bgColor
                    } else {
                        toolBarStateBean = ToolBarStateBean.parseFromString(bgColor)
                    }
                    if (toolBarStateBean != null) {
                        val toolBarBgColor: String? = toolBarStateBean.toolBarBgColor
                        val toolBarTitleColor: String? = toolBarStateBean.toolBarTitleColor
                        var toolBarColor = 0
                        if (!TextUtils.isEmpty(toolBarBgColor)) {
                            toolBarColor = Color.parseColor(toolBarBgColor)
                            mBinding.toolbar.setBackgroundColor(toolBarColor)
                        }
                        var toolBarTitle = defaultTitleStateColor
                        if (!TextUtils.isEmpty(toolBarTitleColor)) {
                            toolBarTitle = Color.parseColor(toolBarTitleColor)
                        }
                        mBinding.webTitle.setTextColor(toolBarTitle)
                        mBinding.webBack.imageTintList = ColorStateList.valueOf(toolBarTitle)
                        mBinding.webClose.imageTintList = ColorStateList.valueOf(toolBarTitle)
                        mBinding.webHelp.imageTintList = ColorStateList.valueOf(toolBarTitle)
                        mBinding.webShared.imageTintList = ColorStateList.valueOf(toolBarTitle)
                    }
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
            LogUtils.w(e.message)
        }
    }

    protected fun initPayWebViewSettings() {
        mBinding.payWebview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true
                }
                isShouldAutoOpenWeb = false
                /**
                 * ????????????????????????????????????(payInterceptorWithUrl),??????????????????
                 */
                val task = PayTask(INSTENCE)
                val isIntercepted: Boolean =
                    task.payInterceptorWithUrl(url, true) { result ->
                        // ??????????????????
                        val url: String = result.getReturnUrl()
                        if (!TextUtils.isEmpty(url)) {
                            runOnUiThread(Runnable {
                                //                                    jumpToPayResultAct();
                            })
                        }
                    }
                /**
                 * ????????????????????????
                 * ??????????????????????????????????????????URL?????????????????????
                 */
                if (!isIntercepted) {   //???????????????sdk?????????url??????????????????
//                    UiUtils.openPhoneWebUrl(this@WebViewActivity, url)
                }
                return true
            }
        }
    }

    fun setOpenId(openId: String) {
        // ???????????????
        if (webView == null) {
            return
        }
        webView!!.loadUrl("javascript:setOpenId('$openId')")
    }

    //    private void disPathPayOrderInfo(PayOrderResponse.DataBean data) {
    //        mOrderNo = data.getOrderPayNo();
    //        mIsOpenVip = data.isOpeningVip();
    //        String payType = data.getPayType();
    //        int result = data.getResult();
    //        if (result == 0) {
    //            String url = data.getUrl();
    //            String params = GsonTool.toJson(data.getParams());
    //            if (PayDialog.PAY_TYPE_ZHIFUBAO.equals(payType) || PayDialog.PAY_TYPE_ZHIFUBAO_2.equals(payType)) {
    //                boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this, url, new H5PayCallback() {
    //                    @Override
    //                    public void onPayResult(H5PayResultModel h5PayResultModel) {
    //                        jumpToPayResultAct();
    //                    }
    //                });
    //                if (!urlCanUse) {
    //                    isShouldAutoOpenWeb = true;
    //                    mBinding.payWebview.loadUrl(url);
    //                    mBinding.payWebview.postDelayed(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (isShouldAutoOpenWeb) {
    //                                UiUtils.openPhoneWebUrl(com.xxjy.jyyh.activity.WebViewActivity.this, url);
    //                            }
    //                        }
    //                    }, 4000);
    //                }
    //            } else if (PayDialog.PAY_TYPE_WEIXIN.equals(payType)) {
    //                WeChatWebPayActivity.openWebPayAct(this, null, params, url);
    //            } else if (PayDialog.PAY_TYPE_WEIXIN_2.equals(payType)) {
    //                WeChatWebNowPayActivity.openWebPayAct(this, null, params, url);
    //            } else if (PayDialog.PAY_TYPE_LIANLIAN.equals(payType)) {
    //                Intent intent = new Intent(this, RechargeWebviewActivity.class);
    //                intent.putExtra("url", url);
    //                intent.putExtra("param", params);
    //                startActivity(intent);
    //
    //            } else {
    //                mBinding.payWebview.loadUrl(url);
    //            }
    //        }
    //    }
    //
    //    private void jumpToPayResultAct() {
    //        if (mIsOpenVip) {
    //            PayResultNewActivity.openPayResultAct(INSTENCE,
    //                    PayResultNewActivity.ORDER_TYPE_RECHARGE_ONE_CARD, mOrderNo, null);
    //        } else {
    //            PayResultNewActivity.openPayResultAct(INSTENCE, mOrderNo);
    //        }
    //    }
    private fun initWebViewSettings() {
        progressBar = findViewById<ProgressBar>(R.id.line_progress)
        X5WebManager.initWebView(this@WebViewActivity, webView)
        webView.addJavascriptInterface(mJsOperation, JsOperation.JS_USE_NAME)
        webView.webChromeClient = object : WebChromeClient() {
            //???????????????????????????????????????????????????TextView?????????
            override fun onProgressChanged(webView: WebView, progress: Int) {
                super.onProgressChanged(webView, progress)
                if (progress == 100) {
                    progressBar.visibility = View.GONE //??????????????????????????????
                } else {
                    progressBar.visibility = View.VISIBLE //????????????????????????????????????
                    progressBar.progress = progress //???????????????
                }
            }

            //??????Web?????????title??????????????????????????????title
            //???????????????????????????????????????????????????onReceiveTitle????????????????????? ??????????????????,
            //?????????????????????onReceiveError??????????????????????????????title
            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                mWebTitle.text = s
                if (!TextUtils.isEmpty(s)) {
                    if (!TextUtils.isEmpty(mLastShowTitle)) {
                        JPushManager.onPageEnd(INSTENCE, mLastShowTitle)
                    }
                    JPushManager.onPageStart(INSTENCE, s)
                    mLastShowTitle = s
                }
            }

            //??????alert????????????html ?????????????????????
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                val builder = AlertDialog.Builder(this@WebViewActivity)
                builder.setTitle("?????????")
                    .setMessage(message)
                    .setPositiveButton("??????", null)

                // ???????????????????????????
                // ??????keycode??????84???????????????
                builder.setOnKeyListener { dialog, keyCode, event ->
                    true
                }
                // ???????????????back????????????
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                result.confirm() // ???????????????????????????????????????confirm,??????????????????????????????????????????
                return true
            }

            //??????confirm?????????
            override fun onJsPrompt(
                view: WebView, url: String, message: String, defaultValue: String,
                result: JsPromptResult
            ): Boolean {
                return true
            }

            //??????prompt?????????
            override fun onJsConfirm(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                return true
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                        return true
                    } catch (e: Exception) {
                        showToastWarning("??????????????????????????????????????????, ????????????????????????")
                    }
                } else if (url.startsWith("alipays://platformapi/startApp?")) {
                    try {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                        return true
                    } catch (e: Exception) {
                        showToastWarning("????????????????????????????????????????????????, ???????????????????????????")
                    }
                } else if (!url.startsWith("http") && !url.startsWith("https")) {
                    try {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                        return true
                    } catch (e: Exception) {
                    }
                    return true
                }
                if (url.endsWith("account") || url.endsWith("account/")) {
                    //                    UiUtils.jumpToHome(WebViewActivity.this, Tool.LOGIN_TO_MY);
                    return true
                } else if (url.endsWith("login/") || url.endsWith("login") || url.endsWith("loginPrev/") || url.endsWith(
                        "loginPrev"
                    )
                ) {
                    isShouldLoadUrl = true
                    //                    UiUtils.toLoginActivity(WebViewActivity.this, Tool.LOGIN_FINISH);
                    return true
                } else {
                    if (!(url.startsWith("http") || url.startsWith("https"))) {
                        //                        try {
                        //                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        //                            startActivity(intent);
                        //                        } catch (ActivityNotFoundException e) {
                        //                        }
                    } else {
                        //                        view.loadUrl(url);
                    }
                }
                return false
                //                } else {
                //                    return super.shouldOverrideUrlLoading(view, url);
                //                }
                //                return super.shouldOverrideUrlLoading(view, url);
            }

            override fun onPageStarted(webView: WebView, s: String, bitmap: Bitmap) {
                super.onPageStarted(webView, s, bitmap)
            }

            override fun onPageFinished(webView: WebView, s: String) {
                super.onPageFinished(webView, s)
                refreshCloseIcon()
            }
        }
    }

    private fun refreshCloseIcon() {
        mWebClose!!.visibility = if (webView!!.canGoBack()) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        when (v.id) {
            -1, R.id.web_back -> onBackClick()
            R.id.web_shared -> {
            }
            R.id.web_help -> {
            }
            R.id.web_close -> finish()
        }
    }

    //??????????????????
    fun onBackClick() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            finish()
        }
    }

    override fun onJsCallListener(callType: Int, content: String?) {
        if (callType == OnJsCallListener.CALL_TYPE_SHARE) {
            shared = GsonUtils.fromJson(content, SharedInfoBean::class.java)
            isShared = true
            sharedLink()
        }
    }

    private fun sharedLink() {
        if (shared != null) {
            var icon: String = shared!!.icon
            if (TextUtils.isEmpty(icon)) {
                icon = ""
            }
//            SharedInfoManager.shareInfo(
//                this, icon, shared.getLink(),
//                shared.getTitle(), shared.getDesc(), umShareListener
//            )
        }
    }

    //???????????????
    fun addToCalendar(result: String?) {
//        try {
//            CalendarBean calendarBean = GsonTool.parseJson(result, CalendarBean.class);
//            CalendarEvent event = new CalendarEvent();
//            event.setStartTime(calendarBean.getStartTime());
//            event.setTitle(calendarBean.getName());
//            event.setCouponId(calendarBean.getCouponId());
//            CalendarManager.requestCalendarPermission(INSTENCE, new OnPermissionSuccess() {
//                @Override
//                public void onPermissionSuccess() {
//                    if (CalendarManager.addCalendarEvent(INSTENCE, event)) {
//                        addToCalendarH5();
//                    }
//                }
//
//                @Override
//                public void onPermissionFiler() {
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
    }

    //???????????????
    fun deleteFromCalendar(result: String?) {
//        try {
//            CalendarBean calendarBean = GsonTool.parseJson(result, CalendarBean.class);
//            CalendarManager.requestCalendarPermission(INSTENCE, new OnPermissionSuccess() {
//                @Override
//                public void onPermissionSuccess() {
//                    if (CalendarManager.deleteCalendarEvent(INSTENCE, calendarBean.getCouponId())) {
//                        deleteFromCalendarH5();
//                    }
//                }
//
//                @Override
//                public void onPermissionFiler() {
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
    }

    private fun deleteFromCalendarH5() {
        val resultH5Method = "javascript:deleteCalendarSuccess()"
        webView!!.loadUrl(resultH5Method)
    }

    private fun addToCalendarH5() {
        val resultH5Method = "javascript:addCalendarSuccess()"
        webView!!.loadUrl(resultH5Method)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isShared) {
//            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showSureDialog() {
//        MakeSurePayResultDialog dialog1 = new MakeSurePayResultDialog(this);
//        dialog1.setOnConfirmListener(new MakeSurePayResultDialog.OnConfirm() {
//            @Override
//            public void onConfirm() {
//                jumpToPayResultAct();
//            }
//        });
//        dialog1.show();
    }

    private val umShareListener: OnSharedAllResultListener = object : OnSharedAllResultListener {
        override fun onSharedSuccess(platform: SHARE_MEDIA?) {
            setSharedResult(SHARED_RESULT_SUCCESS, null)
        }

        override fun onSharedError(platform: SHARE_MEDIA?, t: Throwable) {
            setSharedResult(SHARED_RESULT_ERROR, t.message)
        }

        override fun onStart(platform: SHARE_MEDIA?) {}
        override fun onCancel(platform: SHARE_MEDIA?) {
            setSharedResult(SHARED_RESULT_CANCEL, null)
        }
    }

    private fun setSharedResult(resultType: Int, reason: String?) {
        var resultToast = ""
        var resultH5Method = ""
        var resultId: String = shared!!.id
        if (TextUtils.isEmpty(resultId)) {
            resultId = ""
        }
        when (resultType) {
            SHARED_RESULT_SUCCESS -> {
                resultToast = " ???????????? "
                resultH5Method = "javascript:shareSuccess($resultId)"
            }
            SHARED_RESULT_ERROR -> {
                resultToast = " ???????????? $reason"
                resultH5Method = "javascript:sharefail($resultId)"
            }
            SHARED_RESULT_CANCEL -> resultToast = " ????????????"
        }
        val finalResultToast = resultToast
        val finalResultH5Method = resultH5Method
        val finalResultId = resultId
        runOnUiThread(Runnable {
            if (!TextUtils.isEmpty(finalResultId)) {   //???h5?????????
                webView!!.post {
                    if (!TextUtils.isEmpty(finalResultH5Method)) {
                        webView!!.loadUrl(finalResultH5Method)
                    }
                }
            } else {
                showToastSuccess(finalResultToast)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        if (mJsOperation != null) {
            mJsOperation!!.onDestory()
        }
        clearWebView(mBinding.webview)
        clearWebView(mBinding.payWebview)
        if (!TextUtils.isEmpty(mLastShowTitle)) {
            JPushManager.onPageEnd(INSTENCE, mLastShowTitle)
        }
        super.onDestroy()
    }

    private fun clearWebView(webview: WebView?) {
        try {
            if (webview != null) {
                if (null != webview.parent) {
                    (webview.parent as ViewGroup).removeView(webview)
                }
                webview.removeAllViews()
                webview.destroy()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val BAR_TITLE = "WebView"
        private const val SHARED_RESULT_SUCCESS = 101
        private const val SHARED_RESULT_ERROR = 102
        private const val SHARED_RESULT_CANCEL = 103

        /**
         * ????????????web?????????,??????url??????????????????
         *
         * @param activity
         * @param url
         */
        fun openWebActivity(activity: Context?, url: String?) {
//            NaviActivityInfo.disPathIntentFromUrl(activity, url)
        }

        /**
         * ????????????web?????????,?????????????????????url??????
         *
         * @param activity
         * @param url
         */
        fun openRealUrlWebActivity(activity: Context, url: String?) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("url", url)
            activity.startActivity(intent)
        }
    }
}