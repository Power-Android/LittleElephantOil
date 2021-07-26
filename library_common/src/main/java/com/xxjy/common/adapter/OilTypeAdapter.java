package com.xxjy.common.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.common.R;
import com.xxjy.common.entity.OilTypeEntity;

import java.util.List;

/**
 * @author power
 * @date 2/7/21 7:29 PM
 * @project ElephantOil
 * @description:
 */
public class OilTypeAdapter extends BaseQuickAdapter<OilTypeEntity, BaseViewHolder> {

    public OilTypeAdapter(int layoutResId, @Nullable List<OilTypeEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilTypeEntity item) {
        helper.setText(R.id.item_oil_type_tv, item.getOilTypeName());
        helper.getView(R.id.item_oil_type_tv).setSelected(item.isSelect());
    }
}
