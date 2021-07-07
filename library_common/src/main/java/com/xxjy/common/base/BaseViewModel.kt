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
open class BaseViewModel<M : BaseRepository?>(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    var repository: M?
        protected set

    init {
        repository = createModel()
    }

    private fun createModel(): M? {
        if (repository == null) {
            val modelClass: Class<*>
            val type = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[0] as Class<*>
            } else {
                //如果没有指定泛型参数，则默认使用BaseModel
                BaseRepository::class.java
            }
            try {
                repository = modelClass.newInstance() as M
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
        return repository
    }

    fun loadingView(): LiveData<Boolean> {
        return repository!!.isShowLoading
    }

    override fun onCleared() {
        super.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
        repository!!.unDisposable()
    }

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