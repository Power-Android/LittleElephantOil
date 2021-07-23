package com.xxjy.jyyh.ui.mine

import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.NumberUtils
import com.blankj.utilcode.util.SpanUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.dialog.CustomerServiceDialog
import com.xxjy.common.entity.BannerBean
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.util.GlideUtils
import com.xxjy.common.util.LoginHelper
import com.xxjy.common.util.NotificationsUtils
import com.xxjy.common.util.eventtrackingmanager.EventTrackingManager
import com.xxjy.common.util.eventtrackingmanager.TrackingConstant
import com.xxjy.common.util.eventtrackingmanager.TrackingEventConstant
import com.xxjy.jyyh.base.BindingFragment
import com.xxjy.personal.R
import com.xxjy.personal.adapter.MineTabAdapter
import com.xxjy.personal.databinding.FragmentMineBinding
import com.xxjy.personal.entity.UserBean
import com.xxjy.personal.viewmodel.MineViewModel

class MineFragment : BindingFragment<FragmentMineBinding, MineViewModel>() {
    private var mUserCardId = ""
    private val tabs: List<BannerBean> = ArrayList<BannerBean>()
    private var mineTabAdapter: MineTabAdapter? = null
    private var userBean: UserBean? = null
    private var customerServiceDialog: CustomerServiceDialog? = null
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (UserConstants.login_status) {
                queryUserInfo()
                getMonthEquityInfo()
            }
            getVipInfo()
        }
    }

    override fun onVisible() {
        super.onVisible()
        getBannerOfPosition()
        getVipInfo()
        if (UserConstants.login_status) {
            queryUserInfo()
            getMonthEquityInfo()
        } else {
            mBinding.photoView.setImageResource(R.drawable.default_img_bg)
            mBinding.nameView.text = "登录注册"
            mBinding.userPhoneView.text = ""
            SpanUtils.with(mBinding.couponView)
                .setBold()
                .append("0")
                .append("张")
                .setForegroundColor(Color.parseColor("#323334"))
                .create()
            SpanUtils.with(mBinding.integralView)
                .setBold()
                .append("0") //                    .append("积分")
                //                    .setForegroundColor(Color.parseColor("#323334"))
                .create()
            SpanUtils.with(mBinding.balanceView)
                .setBold()
                .append("0")
                .append("元")
                .setForegroundColor(Color.parseColor("#323334"))
                .create()
        }
        if (UserConstants.gone_integral) {
            mBinding.equityOrderLayout.visibility = View.INVISIBLE
            mBinding.moreServiceLayout.visibility = View.GONE
        } else {
            mBinding.equityOrderLayout.visibility = View.VISIBLE
            mBinding.moreServiceLayout.visibility = View.VISIBLE
        }
        UserConstants.notification_remind_user_center = !NotificationsUtils.isNotificationEnabled(context)
        //            mBinding.noticeLayout.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
        if (UserConstants.notification_remind_user_center) {
            mBinding.noticeLayout.visibility = View.VISIBLE
            mBinding.noticeLayout.animation = AnimationUtils.makeInAnimation(
                context, true
            )
        } else mBinding.noticeLayout.visibility = View.GONE
        setHeight(true)
    }

    public override fun initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout)
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.noticeLayout)
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.userDataLayout)
        if (UserConstants.gone_integral) {
            mBinding.integralLayout.visibility = View.INVISIBLE
        } else {
            mBinding.integralLayout.visibility = View.VISIBLE
        }

        //是否隐藏余额
        mViewModel.getOsBalance().observe(this) { aBoolean ->
            if (!aBoolean) {
                UserConstants.gone_balance=true
                mBinding.balanceLineView.visibility = View.GONE
                mBinding.balanceLayout.visibility = View.GONE
            } else {
                UserConstants.gone_balance=false
                mBinding.balanceLineView.visibility = View.VISIBLE
                mBinding.balanceLayout.visibility = View.VISIBLE
            }
        }
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        mineTabAdapter = MineTabAdapter(R.layout.adapter_mine_tab, tabs)
        mBinding.recyclerView.adapter = mineTabAdapter
        mineTabAdapter!!.setOnItemClickListener { adapter, view, position ->
            LoginHelper.login(object :LoginHelper.CallBack{
                override fun onLogin() {
                    ARouterManager.navigation(RouteConstants.Web.A_WEB).withString(RouteConstants.ParameterKey.URL,(adapter.getItem(position) as BannerBean).link).navigation()
                }
            })
        }
        mBinding.refreshview.setOnRefreshListener { refreshLayout ->
            getBannerOfPosition()
            getVipInfo()
            if (UserConstants.login_status) {
                queryUserInfo()
                getMonthEquityInfo()
            } else {
                mBinding.refreshview.finishRefresh()
            }
        }
    }

    override fun initListener() {
        mBinding.equityOrderLayout.setOnClickListener(::onViewClicked)
        mBinding.refuelingOrderLayout.setOnClickListener(::onViewClicked)
        mBinding.localLifeOrderLayout.setOnClickListener(::onViewClicked)
        mBinding.messageCenterView.setOnClickListener(::onViewClicked)
        mBinding.customerServiceView.setOnClickListener(::onViewClicked)
        mBinding.settingView.setOnClickListener(::onViewClicked)
        mBinding.nameView.setOnClickListener(::onViewClicked)
        mBinding.photoView.setOnClickListener(::onViewClicked)
        mBinding.myCouponLayout.setOnClickListener(::onViewClicked)
        mBinding.balanceLayout.setOnClickListener(::onViewClicked)
        mBinding.integralLayout.setOnClickListener(::onViewClicked)
        mBinding.closeNoticeView.setOnClickListener(::onViewClicked)
        mBinding.openView.setOnClickListener(::onViewClicked)
        mBinding.receiveBt.setOnClickListener(::onViewClicked)
        //        mBinding.vipReceiveBt.setOnClickListener(this::onViewClicked);
        mBinding.vipLayout.setOnClickListener(View.OnClickListener {
            goWeb(Constants.VIP_URL)

            EventTrackingManager.instance?.trackingEvent(
                getBaseActivity(),
                TrackingConstant.HOME_MINE,
                TrackingEventConstant.EVENT_HOME_MINE_VIPCARD
            )
        })
        //        mBinding.vipReceiveBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebViewActivity.openRealUrlWebActivity(getBaseActivity(), Constants.VIP_URL);
//                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(), TrackingConstant.HOME_MINE, TrackingEventConstant.EVENT_HOME_MINE);
//            }
//        });
    }

    override fun onViewClicked(view: View) {
        LoginHelper.login(object : LoginHelper.CallBack {
           override fun onLogin() {
                when (view!!.id) {
                    R.id.equity_order_layout -> {

                    }
//                        activity!!.startActivity(
//                        Intent(
//                            context,
//                            OtherOrderListActivity::class.java
//                        ).putExtra("isIntegral", true)
//                    )
                    R.id.refueling_order_layout -> {

                    }
//                        activity!!.startActivity(
//                        Intent(
//                            context,
//                            OrderListActivity::class.java
//                        )
//                    )
                    R.id.local_life_order_layout ->{

                    }
//                        activity!!.startActivity(
//                            Intent(
//                                context,
//                                CarServeOrderListActivity::class.java
//                            )
//                        )
                    R.id.customer_service_view -> {
                        if (customerServiceDialog == null) {
                            customerServiceDialog = CustomerServiceDialog(getBaseActivity()!!)
                        }
                        customerServiceDialog?.show(view)
                    }
                    R.id.message_center_view -> {

                    }
//                        activity!!.startActivity(
//                        Intent(
//                            context,
//                            MessageCenterActivity::class.java
//                        )
//                    )
                    R.id.setting_view -> {

                    }

//                        activity!!.startActivity(
//                        Intent(
//                            context,
//                            SettingActivity::class.java
//                        )
//                    )
                    R.id.my_coupon_layout -> {

                    }
//                        activity!!.startActivity(
//                        Intent(
//                            context,
//                            MyCouponActivity::class.java
//                        )
//                    )
                    R.id.name_view, R.id.photo_view -> {

                    }
                    R.id.integral_layout -> {
                        if (userBean == null) {
                            return
                        }
                        goWeb(userBean?.integralBillUrl)
                    }
                    R.id.balance_layout -> {
                        if (userBean == null) {
                            return
                        }
                        goWeb(userBean?.walletUrl)
                    }
                    R.id.close_notice_view -> {
                        UserConstants.notification_remind_user_center=false
                        mBinding.noticeLayout.visibility = View.GONE
                        mBinding.noticeLayout.animation = AnimationUtils.makeOutAnimation(
                            context, true
                        )
                    }
                    R.id.open_view -> NotificationsUtils.requestNotify(context!!)
                    R.id.receive_bt -> {
                        if (userBean == null) {
                            return
                        }
                        goWeb(userBean?.monthCardBuyUrl)
                    }
                }
            }
        })
    }

    override fun dataObservable() {
        mViewModel.userLiveData.observe(this) { data ->
            userBean = data
            mBinding.refreshview.finishRefresh()
            GlideUtils.loadCircleImage(context, data.headImg, mBinding.photoView)
            mBinding.nameView.text = data.phone
            mBinding.userPhoneView.text = data.phone
            mBinding.couponView.text = data.couponsSize
            mBinding.integralView.text = NumberUtils.format(
                data.integralBalance!!.toDouble(),
                0
            )
            mBinding.balanceView.text = data.balance
            SpanUtils.with(mBinding.couponView)
                .append("" + data.couponsSize)
                .setBold()
                .append("张")
                .setForegroundColor(Color.parseColor("#323334"))
                .create()
            SpanUtils.with(mBinding.integralView)
                .append("" + NumberUtils.format(data.integralBalance!!.toDouble(), 0))
                .setBold()
                .create()
            SpanUtils.with(mBinding.balanceView)
                .append("" + data.balance)
                .setBold()
                .append("元")
                .setForegroundColor(Color.parseColor("#323334"))
                .create()
            if (TextUtils.isEmpty(data.monthCardExpireDate)) {
                SpanUtils.with(mBinding.cardMoneyView)
                    .append("到手立省")
                    .append(data.totalDiscountPre.toString() + "元")
                    .setForegroundColor(Color.parseColor("#FE1300"))
                    .create()
                mBinding.receiveBt.text = "立即领卡"
            } else {
                SpanUtils.with(mBinding.cardMoneyView2)
                    .append("已省")
                    .append(data.monthCardTotalDiscount.toString() + "元")
                    .setForegroundColor(Color.parseColor("#FE1300"))
                    .create()
                mBinding.receiveBt.text = "前往月卡中心"
                mBinding.cardMoneyView.text = "到期时间：" + data.monthCardExpireDate
            }
        }
        mViewModel.bannersLiveData.observe(this) { data ->
            if (data != null && data.isNotEmpty()) {
                mBinding.moreServiceLayout.visibility = View.VISIBLE
                mineTabAdapter?.setNewData(data)
                mBinding.refreshview.finishRefresh()
            } else {
                mBinding.moreServiceLayout.visibility = View.GONE
            }
        }
        mViewModel.monthEquityInfoLiveData.observe(this) { data ->
            if (data != null) {
                if (data.buymMonthCard) {
                    mBinding.cardMoneyView2.visibility = View.VISIBLE
                    mBinding.cardMoneyView.visibility = View.VISIBLE
                } else {
                    mBinding.cardMoneyView2.visibility = View.INVISIBLE
                    mBinding.cardMoneyView.visibility = View.VISIBLE
                }
            }
        }
        mViewModel.vipInfoLiveData.observe(this) { s ->
            setHeight(true)
            if (TextUtils.isEmpty(s.userCardId)) {
                mBinding.vipMoneyView2.visibility = View.INVISIBLE
                mBinding.vipReceiveBt.setImageResource(R.mipmap.vip_join_iv)
                if (!TextUtils.isEmpty(s.description)) {
                    mBinding.vipMoneyView.text = s.description
                }
            } else {
                mUserCardId = s.userCardId!!
                mBinding.vipMoneyView2.visibility = View.VISIBLE
                SpanUtils.with(mBinding.vipMoneyView2)
                    .append("已省")
                    .append(s.saveMoney.toString() + "元")
                    .setForegroundColor(resources.getColor(R.color.color_4C28))
                    .create()
                mBinding.vipMoneyView.text = "有效期至：" + s.expire
                mBinding.vipReceiveBt.setImageResource(R.mipmap.vip_look_icon)
            }
        }
    }

    private fun setHeight(isHeight: Boolean) {
        if (isHeight) {
            val params: ViewGroup.LayoutParams = mBinding.layout1.layoutParams
            params.height = QMUIDisplayHelper.dpToPx(135)
            mBinding.layout1.layoutParams = params
            val params2: ViewGroup.LayoutParams = mBinding.bgView.layoutParams
            params2.height = QMUIDisplayHelper.dpToPx(135)
            mBinding.bgView.layoutParams = params2
            mBinding.bgView.setBackgroundResource(R.mipmap.bg_mine_top_4)
            mBinding.vipLayout.visibility = View.VISIBLE
        } else {
            val params: ViewGroup.LayoutParams = mBinding.layout1.layoutParams
            params.height = QMUIDisplayHelper.dpToPx(70)
            mBinding.layout1.layoutParams = params
            val params2: ViewGroup.LayoutParams = mBinding.bgView.layoutParams
            params2.height = QMUIDisplayHelper.dpToPx(70)
            mBinding.bgView.layoutParams = params2
            mBinding.bgView.setBackgroundResource(R.mipmap.bg_mine_top_2)
            mBinding.vipLayout.visibility = View.GONE
        }
    }

    private fun goWeb(url:String?){
        ARouterManager.navigation(RouteConstants.Web.A_WEB).withString(RouteConstants.ParameterKey.URL, url).navigation()
    }

    private fun getBannerOfPosition() {
            mViewModel.getBannerOfPosition()
        }

    private fun queryUserInfo() {
        mViewModel.queryUserInfo()
    }

   fun getMonthEquityInfo() {
            mViewModel.getMonthEquityInfo()
        }
    fun getVipInfo() {
            mViewModel.getVipInfo()
        }

    companion object {
        val instance: MineFragment
            get() = MineFragment()
    }
}