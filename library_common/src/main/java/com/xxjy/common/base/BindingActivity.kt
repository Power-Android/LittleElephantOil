package com.xxjy.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.NetworkUtils.NetworkType
import com.xxjy.common.R
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

abstract class BindingActivity<V : ViewBinding, VM : BaseViewModel<*>> : BaseActivity(),
    NetworkUtils.OnNetworkStatusChangedListener {
    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (NetworkUtils.isConnected()) {
            initViewModel()
            initView()
            initListener()
            //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
            dataObservable()
        } else {
            initNoNetWorkView()
        }
        checkNetwork()
    }

    /**
     * 检测网络
     */
    open fun checkNetwork() {
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    override fun onDisconnected() {
        initNoNetWorkView()
    }

    override fun onConnected(networkType: NetworkType?) {
        when (networkType) {
            NetworkType.NETWORK_NO -> initNoNetWorkView()
            else -> {
                initViewModel()
                initView()
                initListener()
                //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
                dataObservable()
            }
        }
    }

    open fun initNoNetWorkView() {
        val view: View =
            LayoutInflater.from(this@BindingActivity).inflate(R.layout.no_network_layout, null)
        setContentView(view)
        val refreshView: TextView = findViewById(R.id.refresh_view)
        refreshView.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                view.visibility = View.GONE
                initViewModel()
                initView()
                initListener()
                //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
                dataObservable()
            }
        }
    }

    /**
     * 初始化viewModel
     */
    open fun initViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            mBinding = (type.actualTypeArguments[0] as Class<V>)
                .getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, layoutInflater) as V
            setContentView(mBinding.root)
        }
        val modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<VM>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            BaseViewModel::class.java as Class<VM>
        }
        mViewModel = ViewModelProvider(this).get(modelClass)

        //让ViewModel绑定View的生命周期
        lifecycle.addObserver(mViewModel)
    }

    /**
     * 页面数据初始化方法
     */
    protected abstract fun initView()

    /**
     * 初始化监听
     */
    protected abstract fun initListener()

    /**
     * @param view 处理点击事件
     */
    protected abstract fun onViewClicked(view: View?)

    /**
     * 处理网络请求回调
     */
    protected abstract fun dataObservable()

    override fun onDestroy() {
        super.onDestroy()
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }
}