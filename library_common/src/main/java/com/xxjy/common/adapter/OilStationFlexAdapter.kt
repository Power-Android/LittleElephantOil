package com.xxjy.common.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xxjy.common.R
import com.xxjy.common.entity.OilEntity
import com.xxjy.common.entity.OilEntity.StationsBean.CzbLabelsBean

/**
 * @author power
 * @date 12/3/20 11:46 AM
 * @project RunElephant
 * @description:
 */
class OilStationFlexAdapter(layoutResId: Int, data: List<CzbLabelsBean>) :
    BaseQuickAdapter<CzbLabelsBean, BaseViewHolder>(layoutResId, data) {
     override fun convert(helper: BaseViewHolder, item: CzbLabelsBean) {
        if (!TextUtils.isEmpty(item.tagIndexDescription)) {
            helper.setText(R.id.item_title_tv, item.tagIndexDescription)
        } else {
            helper.setText(R.id.item_title_tv, item.tagName + "")
        }
    }
}