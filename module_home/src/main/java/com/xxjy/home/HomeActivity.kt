package com.xxjy.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.constants.SPConstants
import com.xxjy.common.constants.UserConstants

@Route(path = "/home/home/HomeActivity")
class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}