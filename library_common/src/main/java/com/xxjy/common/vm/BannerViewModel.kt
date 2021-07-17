package com.xxjy.common.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.entity.BannerBean
import com.xxjy.common.repos.BannerRepository

/**
 * @author power
 * @date 1/21/21 11:58 AM
 * @project ElephantOil
 * @description:
 */
class BannerViewModel(application: Application) : BaseViewModel<BannerRepository>(application) {

    private val bannersLiveData: MutableLiveData<List<BannerBean>> = MutableLiveData<List<BannerBean>>()

    fun bannerOfPosition(position: Int): MutableLiveData<List<BannerBean>> {
        mRepository.getBannerOfPosition(bannersLiveData, position)
        return bannersLiveData
    }
}