package com.xxjy.home.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.home.R
import com.xxjy.home.entity.HomeMenuEntity

/**
 * @author power
 * @date 2021/5/31 3:36 下午
 * @project ElephantOil
 * @description:
 */
class HomeMenuAdapter(layoutResId: Int, data: List<HomeMenuEntity>) :
    BaseQuickAdapter<HomeMenuEntity, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeMenuEntity) {
        Glide.with(mContext)
            .load(item.icon)
            .apply(
                RequestOptions()
                    .error(R.drawable.default_img_bg)
            )
            .into(helper.getView<View>(R.id.item_img_iv) as ImageView)
        helper.setText(R.id.item_title_tv, item.title)
    }
}