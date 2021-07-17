package com.xxjy.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.rxlife.coroutine.RxLifeScope
import com.xxjy.common.provide.MContext
import com.xxjy.common.util.toastlib.Toasty
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope

/**
 * @author power
 * @date 12/1/20 1:17 PM
 * @project RunElephant
 * @description:
 */
open class BaseRepository {
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val mRxLifeScope: RxLifeScope = RxLifeScope()

    /**
     * 请求
     * @param block 请求的主函数体，得到数据后调用onSuccess方法
     * @param isShowLoading 请求时的提示语句，不为空时才开启弹窗提示
     */
    protected fun request(
        block: suspend CoroutineScope.() -> Unit,
        isLoading: Boolean = false
    ){
        mRxLifeScope.launch(
            {
                block()
            },
            {
                //错误回调
                Toasty.error(MContext.context(), it.message.toString())
                it.message
            },
            {
                //请求开始
                if(isLoading) showLoading(true)
            },
            {
                //请求结束
                if(isLoading) showLoading(false)
            }
        )
    }

    private fun showLoading(isShow: Boolean) =
        loadingLiveData.postValue(isShow)

    fun isShowLoading(): LiveData<Boolean> =
        loadingLiveData

}