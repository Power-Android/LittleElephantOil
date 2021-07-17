package com.xxjy.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.constants.SPConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.router.RoutePathConstants

@Route(path = RoutePathConstants.Home.A_HOME)
class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<TextView>(R.id.text).setOnClickListener {
            ARouter.getInstance().build(RoutePathConstants.Main.A_MAIN).navigation()
        }
    }
}