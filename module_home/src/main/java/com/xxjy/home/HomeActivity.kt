package com.xxjy.home

import android.os.Bundle
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.router.RouteConstants

@Route(path = RouteConstants.Home.A_HOME)
class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<TextView>(R.id.text).setOnClickListener {
            ARouter.getInstance().build(RouteConstants.Main.A_MAIN).navigation()
        }
    }
}