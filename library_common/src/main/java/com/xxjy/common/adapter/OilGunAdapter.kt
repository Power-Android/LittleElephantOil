package com.xxjy.common.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.common.R
import com.xxjy.common.entity.OilEntity

/**
 * @author power
 * @date 1/22/21 1:11 PM
 * @project ElephantOil
 * @description:
 */
class OilGunAdapter(
    layoutResId: Int,
    data: List<OilEntity.StationsBean.OilPriceListBean.GunNosBean>
) : BaseQuickAdapter<OilEntity.StationsBean.OilPriceListBean.GunNosBean, BaseViewHolder>(
    layoutResId,
    data
) {
    override fun convert(
        helper: BaseViewHolder,
        item: OilEntity.StationsBean.OilPriceListBean.GunNosBean
    ) {
        helper.setText(R.id.item_oil_type_tv, item.gunNo.toString() + "号枪")
        helper.itemView.isSelected = item.isSelected
    }
}