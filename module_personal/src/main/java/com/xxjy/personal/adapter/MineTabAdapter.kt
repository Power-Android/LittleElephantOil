package com.xxjy.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.common.entity.BannerBean
import com.xxjy.common.util.GlideUtils
import com.xxjy.personal.R

 class MineTabAdapter(layoutResId: Int, data: List<BannerBean>) :
    BaseQuickAdapter<BannerBean?, BaseViewHolder?>(layoutResId, data) {
     override fun convert(helper: BaseViewHolder, item: BannerBean?) {
         item?.let {
             GlideUtils.loadImage(mContext, it.imgUrl, helper.getView(R.id.image_view))
             helper.setText(R.id.title_view, it.title)
         }

    }

}