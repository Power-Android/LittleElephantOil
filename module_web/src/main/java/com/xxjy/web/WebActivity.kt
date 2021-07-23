package com.xxjy.web

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants

class WebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        findViewById<AppCompatButton>(R.id.bt).setOnClickListener {
            ARouterManager.navigation(RouteConstants.Web.A_WEB).withString(RouteConstants.ParameterKey.URL,"https://m.qqgyhk.com/membership/membership?id=1").navigation()
        }
    }
}