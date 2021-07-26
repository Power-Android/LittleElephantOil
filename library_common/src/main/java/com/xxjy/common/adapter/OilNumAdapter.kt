package com.xxjy.common.adapter

import com.xxjy.common.entity.OilEntity.StationsBean.OilPriceListBean
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.common.R

/**
 * @author power
 * @date 1/21/21 9:11 PM
 * @project ElephantOil
 * @description:
 */
class OilNumAdapter(layoutResId: Int, data: List<OilPriceListBean>) :
    BaseQuickAdapter<OilPriceListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: OilPriceListBean) {
        helper.getView<View>(R.id.item_oil_type_tv).isSelected = item.isSelected
        helper.setText(R.id.item_oil_type_tv, item.oilName)
    }
}