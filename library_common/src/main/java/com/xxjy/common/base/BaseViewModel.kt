package com.xxjy.common.base

import android.app.Application
import androidx.lifecycle.*
import java.lang.reflect.ParameterizedType

/**
 * @author power
 * @date 12/1/20 1:17 PM
 * @project RunElephant
 * @description:
 */
open class BaseViewModel<M : BaseRepository> constructor(application: Application) :
    AndroidViewModel(application),
    LifecycleObserver {

    var mRepository: M = createModel()

    private fun createModel(): M {
        val modelClass: Class<*>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[0] as Class<*>
        } else {
            //如果没有指定泛型参数，则默认使用BaseModel
            BaseRepository::class.java
        }
        try {
            mRepository = modelClass.newInstance() as M
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return mRepository
    }

    override fun onCleared() {
        super.onCleared()
        mRepository.mRxLifeScope.close()
    }

    fun loadingView(): LiveData<Boolean> =
        mRepository.isShowLoading()

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
    }

}