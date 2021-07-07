package com.xxjy.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author power
 * @date 12/1/20 1:17 PM
 * @project RunElephant
 * @description:
 */
open class BaseRepository {
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable? = null
    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    protected fun addDisposable(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    /**
     * viewModel销毁时清除Rxjava
     */
    fun unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable!!.isDisposed) {
            mCompositeDisposable!!.clear()
        }
    }

    fun showLoading(isShow: Boolean) =
        loadingLiveData.postValue(isShow)

    val isShowLoading: LiveData<Boolean>
        get() = loadingLiveData

}