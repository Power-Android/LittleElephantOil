package com.xxjy.common.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.common.R
import com.xxjy.common.entity.HomeProductEntity

/**
 * @author power
 * @date 1/21/21 5:47 PM
 * @project ElephantOil
 * @description:
 */
class HomeExchangeAdapter(layoutResId: Int, data: List<HomeProductEntity.FirmProductsVoBean>) :
    BaseQuickAdapter<HomeProductEntity.FirmProductsVoBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeProductEntity.FirmProductsVoBean) {
        Glide.with(mContext).load(item.productImg)
            .into(helper.getView<View>(R.id.item_img_iv) as ImageView)
        helper.setText(R.id.item_title_tv, item.name)
            .setText(R.id.item_integral_tv,
                if (TextUtils.isEmpty(item.redeemPrice) || item.redeemPrice.toDouble() == 0.0)
                    item.redeemPoint + "积分"
                else
                    item.redeemPoint + "积分 + " + item.redeemPrice + "元"
            )
            .setText(R.id.sell_num_view, "已兑换" + item.salesNum.toString() + "件")
    }
}