package com.xxjy.common.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xxjy.common.R

class BindingFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binding_fragment)
    }
}