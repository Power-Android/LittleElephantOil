package com.xxjy.common.repos

import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.entity.BannerBean
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @author power
 * @date 1/21/21 11:59 AM
 * @project ElephantOil
 * @description:
 */
class BannerRepository : BaseRepository() {

    fun getBannerOfPosition(bannersLiveData: MutableLiveData<List<BannerBean>>, position: Int) {
        request({
            bannersLiveData.value =
                RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                    .add("position", position)
                    .toResponse<List<BannerBean>>()
                    .await()
        })
    }
}