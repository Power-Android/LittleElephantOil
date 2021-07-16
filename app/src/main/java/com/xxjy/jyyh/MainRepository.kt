package com.xxjy.jyyh

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import rxhttp.awaitResult
import rxhttp.tryAwait
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toResponse

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
class MainRepository : BaseRepository() {
//    fun getOsOverAll(osLiveData: MutableLiveData<Boolean?>) {
//        addDisposable(RxHttp.postForm(ApiService.GET_OS_OVERALL)
//            .toResponse<Boolean>()
//            .subscribe { b -> osLiveData.postValue(b) }
//        )
//    }

    fun osOver(osLiveData: MutableLiveData<Boolean>){
        request({
            osLiveData.value = RxHttp.postForm(ApiService.GET_OS_OVERALL)
                .toResponse<Boolean>()
                .await()
        }, true)
    }

    suspend fun test(){
        RxHttp.postForm(ApiService.GET_OS_OVERALL)
            .toResponse<Boolean>()
            .awaitResult()
    }

//    public void newUserStatus(MutableLiveData<HomeNewUserBean> newUserLiveData) {
    //        addDisposable(RxHttp.postForm(ApiService.NEW_USER_STATUS)
    //                .add("code","newuser")
    //                .asResponse(HomeNewUserBean.class)
    //                .subscribe(b -> {
    //                    newUserLiveData.postValue(b);
    //                })
    //        );
    //    }
    //    public void getIsNewUser(MutableLiveData<Boolean> isNewLiveData) {
    //        addDisposable(RxHttp.postForm(ApiService.IS_NEW_USER)
    //                .asResponse(Boolean.class)
    //                .subscribe(b -> isNewLiveData.postValue(b))
    //
    //        );
    //    }
}