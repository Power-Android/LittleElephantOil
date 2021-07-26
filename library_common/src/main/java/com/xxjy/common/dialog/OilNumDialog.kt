package com.xxjy.common.dialog

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xxjy.common.R
import com.xxjy.common.adapter.OilGunAdapter
import com.xxjy.common.adapter.OilNumAdapter
import com.xxjy.common.adapter.OilTypeAdapter
import com.xxjy.common.databinding.DialogOilNumLayoutBinding
import com.xxjy.common.entity.OilEntity
import com.xxjy.common.entity.OilTypeEntity
import com.xxjy.common.util.OilUtils
import com.xxjy.common.util.toastlib.MyToast
import java.util.*

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
class OilNumDialog(mContext: Context, stationsBean: OilEntity.StationsBean) :
    BottomSheetDialog(mContext, R.style.bottom_sheet_dialog) {

    private val mStationsBean = stationsBean
    private val mBinding = DialogOilNumLayoutBinding.bind(
        LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_num_layout, null)
    )
    private var mList = ArrayList<OilEntity.StationsBean.OilPriceListBean>()
    private var selectPosition = 0
    private var mOilNo: String? = null
    private val mOilNumGasData = ArrayList<OilEntity.StationsBean.OilPriceListBean>() //汽油
    private val mOilNumDieselData = ArrayList<OilEntity.StationsBean.OilPriceListBean>() //柴油
    private val mOilNumNaturalData = ArrayList<OilEntity.StationsBean.OilPriceListBean>() //天然气
    private val mOilTypeList = ArrayList<OilTypeEntity>() //油类型
    private var mOilNumList = ArrayList<OilEntity.StationsBean.OilPriceListBean>() //油号列表
    private var mOilGunList = ArrayList<OilEntity.StationsBean.OilPriceListBean.GunNosBean>() //油枪列表

    private lateinit var mOilTypeAdapter: OilTypeAdapter
    private lateinit var mOilNumAdapter: OilNumAdapter
    private lateinit var mOilGunAdapter: OilGunAdapter

    init {
        init()
        initData()
    }

    private fun init() {
        window?.attributes?.windowAnimations = R.style.bottom_sheet_dialog
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        setContentView(mBinding.root)
        val behavior = BottomSheetBehavior.from<View>(mBinding.root.parent as View)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initData() {
        if (mStationsBean.oilPriceList.isEmpty()
        ) {
            MyToast.showError(context, "暂无该油站数据")
            return
        }
        mList = mStationsBean.oilPriceList as ArrayList<OilEntity.StationsBean.OilPriceListBean>
        //如果没有选中，默认第一个选中
        for (i in mList.indices) {
            if (mList[i].isSelected) {
                selectPosition = i
                break
            }
        }
        mList[selectPosition].isSelected =true
        mOilNo = mList[selectPosition].oilNo.toString() + ""

        //油类型列表
        mBinding.oilTypeRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mOilTypeAdapter = OilTypeAdapter(R.layout.adapter_oil_num_layout, mOilTypeList)
        mBinding.oilTypeRecyclerView.adapter = mOilTypeAdapter
        mOilTypeAdapter.setOnItemClickListener { adapter, view, position ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onOilTypeClick(
                    adapter,
                    view,
                    position,
                    mOilNumAdapter,
                    mOilGunAdapter
                )
            }
        }

        //油号列表
        mBinding.oilNumRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mOilNumAdapter = OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList)
        mBinding.oilNumRecyclerView.adapter = mOilNumAdapter
        mOilNumAdapter.setOnItemClickListener { adapter, view, position ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onOilNumClick(adapter, view, position, mOilGunAdapter)
            }
        }

        //枪号列表
        mBinding.oilGunRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mOilGunAdapter = OilGunAdapter(R.layout.adapter_oil_num_layout, mOilGunList)
        mBinding.oilGunRecyclerView.adapter = mOilGunAdapter
        mOilGunAdapter.setOnItemClickListener { adapter, view, position ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onOilGunClick(adapter, view, position)
            }
        }
        dispatchOilData(mStationsBean)
        mBinding.queryTv.setOnClickListener { view ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onQuickClick(view, mOilNumAdapter, mOilGunAdapter)
            }
        }
        mBinding.cancelIv.setOnClickListener {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.closeAll()
            }
        }
    }

    private fun dispatchOilData(stationsBean: OilEntity.StationsBean) {
        //清空数据
        mOilTypeList.clear()
        mOilNumGasData.clear()
        mOilNumDieselData.clear()
        mOilNumNaturalData.clear()
        for (oilNumBean in stationsBean.oilPriceList) {
            if (OilUtils.isOilNumDiesel(oilNumBean)) { //柴油
                mOilNumDieselData.add(oilNumBean)
            } else if (OilUtils.isOilNumNatural(oilNumBean)) { //天然气
                mOilNumNaturalData.add(oilNumBean)
            } else { //汽油
                mOilNumGasData.add(oilNumBean)
            }
        }
        if (mOilNumGasData.isNotEmpty()) {
            mOilTypeList.add(OilTypeEntity("汽油", mOilNumGasData))
        }
        if (mOilNumDieselData.isNotEmpty()) {
            mOilTypeList.add(OilTypeEntity("柴油", mOilNumDieselData))
        }
        if (mOilNumNaturalData.isNotEmpty()) {
            mOilTypeList.add(OilTypeEntity("天然气", mOilNumNaturalData))
        }
        for (i in stationsBean.oilPriceList.indices) {
            if (java.lang.String.valueOf(
                    stationsBean.oilPriceList[i].oilName
                ) == mOilNo
            ) {
                val oilType: Int = stationsBean.oilPriceList[i].oilType
                checkOilType(oilType)
            }
        }

        //已选择列表
        for (i in mOilTypeList.indices) {
            if (mOilTypeList[i].isSelect) {
                val oilPriceList: List<OilEntity.StationsBean.OilPriceListBean> = mOilTypeList[i].oilPriceList
                for (j in oilPriceList.indices) {
                    if (java.lang.String.valueOf(oilPriceList[j].oilNo) == mOilNo) {
//                        mBinding.oilNumTv.setText(oilPriceList.get(j).getOilName());
//                        mBinding.oilLiterTv.setText("¥" + oilPriceList.get(j).getPriceYfq() + "/L");
//                        mBinding.oilPriceTv.setText("油站价：￥" + oilPriceList.get(j).getPriceOfficial());
//                        mBinding.oilPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//                        mOilCheckedList.add(oilPriceList.get(j).getOilName() +
//                                getOilKind(oilPriceList.get(j).getOilType() + ""));
                    }
                }
                //更新油号列表
                mOilNumList = mOilTypeList[i].oilPriceList as ArrayList<OilEntity.StationsBean.OilPriceListBean>
                mOilNumAdapter.setNewData(mOilNumList)
            }
        }

        //油号列表
        for (k in mOilNumList.indices) {
            if (TextUtils.equals(
                    java.lang.String.valueOf(mOilNumList[k].oilNo),
                    mOilNo.toString()
                )
            ) {
                mOilNumList[k].isSelected = true
                //更新油枪列表
                mOilGunList = mOilNumList[k].gunNos as ArrayList<OilEntity.StationsBean.OilPriceListBean.GunNosBean>
                mOilGunAdapter.setNewData(mOilGunList)
            }
        }
    }

    private fun checkOilType(oilType: Int) {
        if (oilType == 1) {
            for (j in mOilTypeList.indices) {
                if (mOilTypeList[j].oilTypeName.equals("汽油")) {
                    mOilTypeList[j].isSelect = true
                }
            }
        }
        if (oilType == 2) {
            for (j in mOilTypeList.indices) {
                if (mOilTypeList[j].oilTypeName == "柴油") {
                    mOilTypeList[j].isSelect = true
                }
            }
        }
        if (oilType == 3) {
            for (j in mOilTypeList.indices) {
                if (mOilTypeList[j].oilTypeName == "天然气") {
                    mOilTypeList[j].isSelect =true
                }
            }
        }
        mOilTypeAdapter.setNewData(mOilTypeList)
    }

    interface OnItemClickedListener {
        //油类型
        fun onOilTypeClick(
            adapter: BaseQuickAdapter<*, *>?,
            view: View?,
            position: Int,
            oilNumAdapter: OilNumAdapter?,
            oilGunAdapter: OilGunAdapter?
        )

        //油号
        fun onOilNumClick(
            adapter: BaseQuickAdapter<*, *>?,
            view: View?,
            position: Int,
            oilGunAdapter: OilGunAdapter?
        )

        //枪号
        fun onOilGunClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int)
        fun onQuickClick(view: View?, oilNumAdapter: OilNumAdapter?, oilGunAdapter: OilGunAdapter?)
        fun closeAll()
    }

    private var mOnItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener?) {
        mOnItemClickedListener = onItemClickedListener
    }
}