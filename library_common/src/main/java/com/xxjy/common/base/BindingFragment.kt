package com.xxjy.jyyh.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.xxjy.common.base.BaseFragment
import com.xxjy.common.base.BaseViewModel
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * @author power
 * @date 12/1/20 1:27 PM
 * @project RunElephant
 * @description:
 */
abstract class BindingFragment<V : ViewBinding, VM : BaseViewModel<*>> : BaseFragment() {
    protected var mBinding: V? = null
    protected var mViewModel: VM? = null
    protected var mContext: Context? = null
    private var contentView: View? = null

    //是否首次显示
    private var mIsFirstVisile = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //私有的初始化DataBinding和ViewModel方法
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (contentView == null) {
            contentView = mBinding!!.root
        }
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        dataObservable()
    }

    /**
     * 初始化viewModel
     */
    private fun initViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            mBinding = (type.actualTypeArguments[0] as Class<V>)
                .getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, layoutInflater) as V
        }
        if (mViewModel == null) {
            val modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<VM>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel::class.java as Class<VM>
            }
            mViewModel = ViewModelProvider(this).get(modelClass)

        }
        //让ViewModel绑定View的生命周期
        lifecycle.addObserver(mViewModel!!)
    }

    override fun onResume() {
        super.onResume()
        if (mIsFirstVisile) {
            mIsFirstVisile = false
            onFirstVisible()
            onVisible()
        } else if (isVisible()) {
            onNotFirstVisible()
            onVisible()
        } else {
            onInvisible()
        }
    }

    /**
     * 该界面首次可见
     */
    protected fun onFirstVisible() {}

    /**
     * 该界面可见，单并非首次可见
     */
    protected fun onNotFirstVisible() {}

    /**
     * 该界面可见
     */
    protected fun onVisible() {}

    /**
     * 该界面不可见
     */
    protected fun onInvisible() {}

    /**
     * 页面数据初始化方法
     */
    protected abstract fun initView()

    /**
     * 初始化监听
     */
    protected abstract fun initListener()

    /**
     * @param view
     * 处理点击事件
     */
    protected fun onViewClicked(view: View?) {}

    /**
     * 处理网络请求回调
     */
    protected fun dataObservable() {}

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}