package com.xxjy.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.DPoint
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.xxjy.common.adapter.HomeExchangeAdapter
import com.xxjy.common.adapter.MyViewPagerAdapter
import com.xxjy.common.adapter.OilStationFlexAdapter
import com.xxjy.common.constants.BannerPositionConstants
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.databinding.HomeCarCardLayoutBinding
import com.xxjy.common.databinding.HomeOilCardLayoutBinding
import com.xxjy.common.dialog.OilNumDialog
import com.xxjy.common.entity.*
import com.xxjy.common.util.LoginHelper
import com.xxjy.common.util.eventtrackingmanager.EventTrackingManager
import com.xxjy.common.util.eventtrackingmanager.TrackingConstant
import com.xxjy.common.util.eventtrackingmanager.TrackingEventConstant
import com.xxjy.common.vm.BannerViewModel
import com.xxjy.common.weight.SettingLayout
import com.xxjy.home.adapter.HomeMenuAdapter
import com.xxjy.home.adapter.HomeTopLineAdapter
import com.xxjy.home.databinding.FragmentHomeBinding
import com.xxjy.home.entity.*
import com.xxjy.home.entity.HomeProductEntity
import com.xxjy.jyyh.base.BindingFragment
import com.xxjy.navigation.MapIntentUtils
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RectangleIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author power
 * @date 1/21/21 11:45 AM
 * @project ElephantOil
 * @description:
 */
class HomeFragment : BindingFragment<FragmentHomeBinding, HomeViewModel>(),
    OnRefreshLoadMoreListener {

    private var mOilTagList = ArrayList<OilEntity.StationsBean.CzbLabelsBean>()
    private var mOftenList = ArrayList<OfentEntity>()
    private var mExchangeList = ArrayList<HomeProductEntity.FirmProductsVoBean>()
    private var mOilNumDialog: OilNumDialog? = null
    private var mLng = 0.0
    private var mLat = 0.0
    private lateinit var mStationsBean: OilEntity.StationsBean
    private lateinit var mFlexAdapter: OilStationFlexAdapter
    private lateinit var mExchangeAdapter: HomeExchangeAdapter
    private var isFar = false //油站是否在距离内
    private var isPay = false //油站是否在距离内 是否显示继续支付按钮
    private var mGasStationTipsDialog: GasStationLocationTipsDialog? = null
    private var mLocationTipsDialog: LocationTipsDialog? = null
    private var bannerViewModel: BannerViewModel? = null
    private var mOilGunPosition = -1
    private var mOilNo = 0
    private var isShowAmount = false
    private var couponId = ""
    private var mOilMonthRuleDialog: OilMonthRuleDialog? = null
    private var mineViewModel: MineViewModel? = null
    private var titles = arrayOf("油站", "车服")
    private var mList = ArrayList<View>()
    private lateinit var mOilView: View
    private lateinit var mCarView: View
    private lateinit var mOilCardBinding: HomeOilCardLayoutBinding
    private lateinit var mCarCardBinding: HomeCarCardLayoutBinding
    private var menuList: List<HomeMenuEntity> = ArrayList<HomeMenuEntity>()
    private lateinit var mHomeMenuAdapter: HomeMenuAdapter
    private var mCardStoreInfoVo: CardStoreInfoVoBean? = null
    private var mPagerAdapter: MyViewPagerAdapter? = null
    private lateinit var mCommonNavigator: CommonNavigator
    private var mCarServiceDialog: CarServiceDialog? = null
    private var mStoreRecordVo: CarServeStoreDetailsBean? = null
    private var mCarOftenList: MutableList<OftenCarsEntity> = ArrayList<OftenCarsEntity>()

    //猎人码跳转
    @BusUtils.Bus(tag = EventConstants.EVENT_JUMP_HUNTER_CODE, sticky = true)
    fun onHunterEvent(oilId: String) {
        if (!TextUtils.isEmpty(oilId)) {
            Constants.HUNTER_GAS_ID = oilId
        }
        homeOil()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getBaseActivity()?.setTransparentStatusBar()
            location()
            EventTrackingManager.instance?.tracking(
                getBaseActivity(),
                TrackingConstant.HOME_MAIN, "gas_id=" + mStationsBean?.gasId
            )
            mViewModel.refuelJob()
            if (UserConstants.login_status) {
                mViewModel.oftenOils()
                mViewModel.oftenCars()
            }
        }
    }

    override fun onVisible() {
        super.onVisible()
        mViewModel.refuelJob()
        if (PermissionUtils.isGranted(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            location()
            if (UserConstants.login_status) {
                mViewModel.oftenOils()
                mViewModel.oftenCars()
            }
        } else {
            if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) { //是否显示首页卡片
                mOilCardBinding.noLocationLayout.visibility = View.VISIBLE
                mOilCardBinding.recommendStationLayout.visibility = View.GONE
            } else {
                mOilCardBinding.noLocationLayout.visibility = View.GONE
                mOilCardBinding.recommendStationLayout.visibility = View.VISIBLE
            }
            mOftenList.clear()
            mCarOftenList.clear()
            mBinding.oftenOilRecyclerView.visibility = View.GONE
            mBinding.oftenCarRecyclerView.visibility = View.GONE
        }
    }

    override fun initView() {
        getBaseActivity()?.setTransparentStatusBar()
        BusUtils.register(this)
        bannerViewModel = ViewModelProvider(this).get(BannerViewModel::class.java)
        loadBanner()
        //tab
        initMagicIndicator()

        if (PermissionUtils.isGranted(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            location()
        } else {
            mOilCardBinding.noLocationLayout.visibility = View.VISIBLE
            mOilCardBinding.recommendStationLayout.visibility = View.GONE
            mCarCardBinding.carNoLocationLayout.visibility = View.VISIBLE
            mCarCardBinding.carLayout.visibility = View.GONE
        }
        homeOil()
//        requestPermission();

        //油站标签
        val flexboxLayoutManager = FlexboxLayoutManager(mContext)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        mOilCardBinding.tagRecyclerView.layoutManager = flexboxLayoutManager
        mFlexAdapter = OilStationFlexAdapter(R.layout.adapter_oil_station_tag, mOilTagList)
        mOilCardBinding.tagRecyclerView.adapter = mFlexAdapter

        //菜单
        mBinding.menuRecyclerView.layoutManager = GridLayoutManager(mContext, 4)
        mHomeMenuAdapter = HomeMenuAdapter(R.layout.adapter_home_menu, menuList)
        mBinding.menuRecyclerView.adapter = mHomeMenuAdapter
        mHomeMenuAdapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.data as List<HomeMenuEntity>
            //TODO LATE
//            when (data[position].type) {
//                1 -> BusUtils.postSticky(
//                    EventConstants.EVENT_CHANGE_FRAGMENT,
//                    EventEntity(EventConstants.EVENT_TO_OIL_FRAGMENT)
//                )
//                2 -> BusUtils.postSticky(
//                    EventConstants.EVENT_CHANGE_FRAGMENT,
//                    EventEntity(EventConstants.EVENT_TO_CAR_FRAGMENT)
//                )
//                3, 4 -> WebViewActivity.openRealUrlWebActivity(
//                    getBaseActivity(),
//                    data[position].url
//                )
//                else -> {
//                }
//            }
        }
        mViewModel.menu()

        //积分豪礼
        mBinding.exchangeRecyclerView.layoutManager =
            LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        mExchangeAdapter = HomeExchangeAdapter(R.layout.adapter_home_exchange, mExchangeList)
        mBinding.exchangeRecyclerView.adapter = mExchangeAdapter
        mExchangeAdapter.setOnItemClickListener { adapter, view, position ->
            TrackingConstant.OIL_MAIN_TYPE = "4"
            //TODO LATE
//            NaviActivityInfo.disPathIntentFromUrl(
//                activity as MainActivity,
//                (adapter.getData().get(position) as HomeProductEntity.FirmProductsVoBean).link
//            )
        }
        mViewModel.homeProduct()
        mOilCardBinding.tagBanner.setAdapter(HomeTopLineAdapter(ArrayList(), false))
            .setOrientation(Banner.VERTICAL)
            .setUserInputEnabled(false)
        mineViewModel = ViewModelProvider(this).get(MineViewModel::class.java)
        adInfo()
    }

    private fun initMagicIndicator() {
        mOilView = View.inflate(mContext, R.layout.home_oil_card_layout, null)
        mCarView = View.inflate(mContext, R.layout.home_car_card_layout, null)
        mList.add(mOilView)
        mList.add(mCarView)
        mOilCardBinding = HomeOilCardLayoutBinding.bind(mOilView)
        mCarCardBinding = HomeCarCardLayoutBinding.bind(mCarView)
        mPagerAdapter = MyViewPagerAdapter(titles, mList)
        mBinding.viewPager.offscreenPageLimit = 1
        mBinding.viewPager.adapter = mPagerAdapter
        mCommonNavigator = CommonNavigator(mContext)
        mBinding.indicator.visibility = View.VISIBLE
        mCommonNavigator.isFollowTouch = true
        mCommonNavigator.isAdjustMode = true
        val padding = 70
        mCommonNavigator.leftPadding = padding
        mCommonNavigator.rightPadding = padding
        mCommonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = SettingLayout(context)
                simplePagerTitleView.text = titles[index]
                simplePagerTitleView.setTextViewSize(17, 17)
                simplePagerTitleView.setSelectedStyle(true)
                simplePagerTitleView.setmNormalColor(resources.getColor(R.color.color_0202))
                simplePagerTitleView.setmSelectedColor(resources.getColor(R.color.color_0202))
                simplePagerTitleView.setOnClickListener {
                    mBinding.viewPager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 25.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 10.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.color_76FF))
                return indicator
            }
        }
        mBinding.indicator.navigator = mCommonNavigator
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager)
    }

    private fun orderMsg() {
        val oilViewModel: OilViewModel = ViewModelProvider(this).get(
            OilViewModel::class.java
        )
        oilViewModel.getOrderNews()
        oilViewModel.orderNewsLiveData.observe(this) { data ->
            if (data != null && data.size() > 0) {
                mOilCardBinding.tagBanner.visibility = View.VISIBLE
                mOilCardBinding.tagRecyclerView.visibility = View.INVISIBLE
                mOilCardBinding.tagBanner.setDatas(data)
            } else {
                mOilCardBinding.tagBanner.setVisibility(View.GONE)
            }
        }
    }

    private fun queryUserInfo() {
        mineViewModel.queryUserInfo()
    }

    private fun homeOil() {
        if (mLng == 0.0 || mLat == 0.0) {
            if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {
                mOilCardBinding.recommendStationLayout.visibility = View.GONE
                mOilCardBinding.noLocationLayout.visibility = View.VISIBLE
            } else {
                mViewModel.homeCard(mLat, mLng, Constants.HUNTER_GAS_ID)
            }
            mCarCardBinding.carNoLocationLayout.visibility = View.VISIBLE
            mCarCardBinding.carLayout.visibility = View.GONE
        } else {
            //首页卡片
            mViewModel.homeCard(mLat, mLng, Constants.HUNTER_GAS_ID)
        }
    }

    private fun loadBanner() {
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.HOME_BANNER)
            .observe(this) { data ->
                if (data != null && data.size() > 0) {
                    //banner
                    mBinding.topBanner
                        .setAdapter(object : BannerImageAdapter<BannerBean>(data) {
                            override fun onBindView(
                                holder: BannerImageHolder,
                                data: BannerBean,
                                position: Int,
                                size: Int
                            ) {
                                Glide.with(holder.imageView)
                                    .load(data.imgUrl)
                                    .apply(
                                        RequestOptions()
                                            .placeholder(R.drawable.bg_banner_loading)
                                            .error(R.drawable.bg_banner_error)
                                    )
                                    .into(holder.imageView)
                                holder.imageView.setOnClickListener(View.OnClickListener { v: View ->
                                    if (TextUtils.isEmpty(data.link)) {
                                        return@OnClickListener
                                    }
                                    if (data.link.contains("/monthCard")) { //月卡
                                        LoginHelper.login { queryUserInfo() }
                                        EventTrackingManager.instance?.trackingEvent(
                                            getBaseActivity(),
                                            TrackingConstant.HOME_MAIN,
                                            TrackingEventConstant.EVENT_HOME_MAIN_MOUTHCARDBANNER
                                        )
                                    } else if (data.link.contains("/inviteFriends")) { //邀请好友
                                        LoginHelper.login {
//                                            WebViewActivity.openWebActivity(
//                                                getActivity() as MainActivity?,
//                                                data.getLink()
//                                            )
                                        }
                                        EventTrackingManager.instance?.trackingEvent(
                                            getBaseActivity(),
                                            TrackingConstant.HOME_MAIN,
                                            TrackingEventConstant.EVENT_HOME_MAIN_INVITEFRIENDBANNER
                                        )
                                    } else if (data.link.contains("?appId=Orvay1rVsoU9nlpY")) { //洗车
                                        BusUtils.postSticky(
                                            EventConstants.EVENT_CHANGE_FRAGMENT,
                                            EventEntity(EventConstants.EVENT_TO_CAR_FRAGMENT)
                                        )
                                        EventTrackingManager.instance?.trackingEvent(
                                            getBaseActivity(),
                                            TrackingConstant.HOME_MAIN,
                                            TrackingEventConstant.EVENT_HOME_MIAN_WASHCARBANNER
                                        )
                                    } else if (data.link.contains("/membership")) { //会员
//                                        WebViewActivity.openWebActivity(
//                                            getActivity() as MainActivity?,
//                                            data.getLink()
//                                        )
                                        EventTrackingManager.instance?.trackingEvent(
                                            getBaseActivity(),
                                            TrackingConstant.HOME_MAIN,
                                            TrackingEventConstant.EVENT_HOME_MAIN_VIPCARD_BANNER
                                        )
                                    } else if (data.link.contains("/luckyDraw")) { //抽奖
//                                        WebViewActivity.openWebActivity(
//                                            getActivity() as MainActivity?,
//                                            data.getLink()
//                                        )
                                        EventTrackingManager.instance?.trackingEvent(
                                            getBaseActivity(),
                                            TrackingConstant.HOME_MAIN,
                                            TrackingEventConstant.EVENT_HOME_MAIN_LOTTERYBANNER
                                        )
                                    } else {
                                        TrackingConstant.OIL_MAIN_TYPE = "3"
//                                        WebViewActivity.openWebActivity(
//                                            getActivity() as MainActivity?,
//                                            data.getLink()
//                                        )
                                    }
                                })
                            }
                        })
                        .setIndicator(RectangleIndicator(mContext))
                        .setIndicatorSpace(resources.getDimension(R.dimen.dp_4) as Int)
                        .setIndicatorHeight(resources.getDimension(R.dimen.dp_4) as Int)
                        .setIndicatorNormalWidth(resources.getDimension(R.dimen.dp_4) as Int)
                        .setIndicatorSelectedWidth(resources.getDimension(R.dimen.dp_8) as Int)
                        .setIndicatorNormalColor(resources.getColor(R.color.color_2D))
                        .setIndicatorSelectedColor(resources.getColor(R.color.color_76FF))
                        .setIndicatorMargins(
                            IndicatorConfig.Margins(
                                0, 0,
                                0, resources.getDimension(R.dimen.dp_55) as Int
                            )
                        )
                        .addBannerLifecycleObserver(this)
                } else {
//                mBinding.topBanner.setVisibility(View.GONE);
                }
            }
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.MARKETING_CENTER)
            .observe(this) { data ->
                if (data != null && data.size() > 0) {
                    mBinding.banner2.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(data.get(0).getImgUrl())
                        .apply(
                            RequestOptions()
                                .placeholder(R.drawable.bg_banner_loading)
                                .error(R.drawable.bg_banner_error)
                        )
                        .into<Target<Drawable>>(mBinding.banner2)
                    mBinding.banner2.setOnClickListener {
                        LoginHelper.login {
//                            TrackingConstant.OIL_MAIN_TYPE = "2"
//                            WebViewActivity.openWebActivity(
//                                getActivity() as MainActivity?,
//                                data.get(0).getLink()
//                            )
                        }
                    }
                } else {
                    mBinding.banner2.visibility = View.GONE
                }
            }
    }

    private fun requestPermission() {
        PermissionUtils.permission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(granted: List<String>) {
//                        mViewModel.getLocation();
                    location()
                }

                override fun onDenied(deniedForever: List<String>, denied: List<String>) {
                    if (deniedForever.isNotEmpty()) {
                        //添加确定按钮
                        //添加返回按钮
                        //dialog消失后重新查询权限
                        AlertDialog.Builder(mContext).setTitle("定位权限被拒绝") //设置对话框标题
                            .setMessage("定位权限被拒绝，将导致部分功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权") { dialog: DialogInterface, which: Int ->  //确定按钮的响应事件
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts(
                                    "package",
                                    mContext.packageName,
                                    null
                                )
                                intent.data = uri
                                startActivity(intent)
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog: DialogInterface, which: Int ->  //响应事件
                                // TODO Auto-generated method stub
                                dialog.dismiss()
                            }
                            .setOnCancelListener { dialog: DialogInterface? -> }
                            .show() //在按键响应事件中显示此对话框
                    } else {
                        mLat = 0.0
                        mLng = 0.0
                        //mViewModel.getHomeOil(mLat, mLng, Constants.HUNTER_GAS_ID);
                        homeOil()
                        showFailLocation()
                        //showToastWarning("权限被拒绝，部分产品功能将无法使用！");
                    }
                }
            })
            .request()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected override fun initListener() {
//        mBinding.scrollView.setOnScrollChangeListener(
//                (View.OnScrollChangeListener) (view, x, y, oldX, oldY) -> {
//                    if (y > BarUtils.getStatusBarHeight()) {
//                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#1676FF"));
//                        mBinding.locationIv.setVisibility(View.INVISIBLE);
//                        mBinding.addressTv.setVisibility(View.INVISIBLE);
//                        mBinding.titleTv.setVisibility(View.VISIBLE);
//                        mBinding.searchIv.setImageResource(R.drawable.icon_search_white);
//                    } else {
//                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#F5F5F5"));
//                        mBinding.locationIv.setVisibility(View.VISIBLE);
//                        mBinding.addressTv.setVisibility(View.VISIBLE);
//                        mBinding.titleTv.setVisibility(View.INVISIBLE);
//                        mBinding.searchIv.setImageResource(R.drawable.icon_search);
//                    }
//                });
        ClickUtils.applySingleDebouncing(
            arrayOf<View>(
                mOilCardBinding.goMoreOilView,
                mCarCardBinding.goMoreCarView
            ), { view: View -> onViewClicked(view) })
        mOilCardBinding.quickOilTv.setOnClickListener(::onViewClicked)
        mOilCardBinding.oilview.setOnClickListener(::onViewClicked)
        mOilCardBinding.oilNumTv.setOnClickListener(::onViewClicked)
        mCarView.findViewById<View>(R.id.quick_car_tv)
            .setOnClickListener { view: View -> onViewClicked(view) }
        mCarView.findViewById<View>(R.id.carview)
            .setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.searchIv.setOnClickListener(::onViewClicked)
        mBinding.awardTv.setOnClickListener(::onViewClicked)
        mOilCardBinding.oilNavigationTv.setOnClickListener(::onViewClicked)
        mBinding.locationIv.setOnClickListener(::onViewClicked)
        mBinding.addressTv.setOnClickListener(::onViewClicked)
        ClickUtils.applySingleDebouncing(
            arrayOf<View>(
                mOilCardBinding.quickOilTv,
                mOilCardBinding.oilNumTv
            )
        ) { view: View -> onViewClicked(view) }
        mBinding.refreshView.setEnableLoadMore(false)
        mBinding.refreshView.setOnRefreshLoadMoreListener(this)
        mBinding.goIntegralView.setOnClickListener(::onViewClicked)
        mBinding.signInIv.setOnClickListener(::onViewClicked)
        mOilCardBinding.goMoreOilView.setOnClickListener(::onViewClicked)
        mOilCardBinding.goLocationView.setOnClickListener(::onViewClicked)
        mCarView.findViewById<View>(R.id.go_more_car_view)
            .setOnClickListener { view: View -> onViewClicked(view) }
        mCarView.findViewById<View>(R.id.car_go_location_view)
            .setOnClickListener { view: View -> onViewClicked(view) }
        mCarView.findViewById<View>(R.id.car_navigation_tv)
            .setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> if (mOftenList.size > 1) {
                        mBinding.oftenOilRecyclerView.visibility = View.VISIBLE
                        mBinding.oftenCarRecyclerView.visibility = View.GONE
                    } else {
                        mBinding.oftenOilRecyclerView.visibility = View.GONE
                        mBinding.oftenCarRecyclerView.visibility = View.GONE
                    }
                    1 -> if (mCarOftenList.size > 1) {
                        mBinding.oftenOilRecyclerView.visibility = View.GONE
                        mBinding.oftenCarRecyclerView.visibility = View.VISIBLE
                    } else {
                        mBinding.oftenOilRecyclerView.visibility = View.GONE
                        mBinding.oftenCarRecyclerView.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onViewClicked(view: View) {
        when (view.id) {
            R.id.quick_oil_tv, R.id.oil_num_tv -> {
                LoginHelper.login {
                    if (mStationsBean.oilPriceList.isNotEmpty()) {
                        showNumDialog(mStationsBean)
                    } else {
                        showToastWarning("暂无加油信息~")
                    }
                }
                EventTrackingManager.instance?.trackingEvent(
                    getBaseActivity(),
                    TrackingConstant.HOME_MAIN,
                    TrackingEventConstant.EVENT_HOME_GASBOXONE
                )
            }
            R.id.oilview -> LoginHelper.login {
//                val intent = Intent(mContext, OilDetailsActivity::class.java)
//                intent.putExtra(Constants.GAS_STATION_ID, mStationsBean.gasId)
//                intent.putExtra(
//                    Constants.OIL_NUMBER_ID,
//                    mStationsBean.oilNo + ""
//                )
//                startActivity(intent)
            }
//            R.id.search_iv -> startActivity(Intent(mContext, SearchActivity::class.java))
            R.id.go_more_oil_view -> BusUtils.postSticky(
                EventConstants.EVENT_CHANGE_FRAGMENT,
                EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT)
            )
            R.id.oil_navigation_tv -> LoginHelper.login {
                if (MapIntentUtils.isPhoneHasMapNavigation(mContext)) {
                    val navigationDialog = NavigationDialog(
                        getBaseActivity(),
                        mStationsBean.stationLatitude, mStationsBean.stationLongitude,
                        mStationsBean.gasName
                    )
                    navigationDialog.show()
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装")
                }
            }
            R.id.car_navigation_tv -> LoginHelper.login {
                if (MapIntentUtils.isPhoneHasMapNavigation(mContext)) {
                    val navigationDialog = NavigationDialog(
                        getBaseActivity(),
                        mStoreRecordVo?.cardStoreInfoVo?.latitude,
                        mStoreRecordVo?.cardStoreInfoVo?.longitude,
                        mStoreRecordVo?.cardStoreInfoVo?.address
                    )
                    navigationDialog.show()
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装")
                }
            }
            R.id.location_iv, R.id.address_tv -> {
            }
            R.id.go_integral_view -> BusUtils.postSticky(
                EventConstants.EVENT_CHANGE_FRAGMENT,
                EventEntity(EventConstants.EVENT_TO_INTEGRAL_FRAGMENT)
            )
//            R.id.sign_in_iv -> LoginHelper.login{
//                TrackingConstant.OIL_MAIN_TYPE = "5"
//                WebViewActivity.openRealUrlWebActivity(getBaseActivity(), Constants.SIGN_IN_URL)
//            }
            R.id.car_go_location_view, R.id.go_location_view -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            R.id.quick_car_tv -> LoginHelper.login {
                if (mStoreRecordVo != null && mStoreRecordVo?.cardStoreInfoVo != null) {
                    mStoreRecordVo?.let { showCarDialog(mStoreRecordVo!!) }
                }
            }
//            R.id.carview -> LoginHelper.login{
//                val intent1 = Intent(mContext, CarServeDetailsActivity::class.java)
//                intent1.putExtra("no", mStoreRecordVo.getCardStoreInfoVo().getStoreNo())
//                intent1.putExtra("distance", mStoreRecordVo.getCardStoreInfoVo().getDistance())
//                intent1.putExtra("channel", mStoreRecordVo.getCardStoreInfoVo().getChannel())
//                startActivity(intent1)
//            }
            R.id.go_more_car_view -> BusUtils.postSticky(
                EventConstants.EVENT_CHANGE_FRAGMENT,
                EventEntity(EventConstants.EVENT_TO_CAR_FRAGMENT)
            )
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun dataObservable() {
        mViewModel.locationLiveData.observe(this, {
            UserConstants.latitude = it.lat.toString()
            UserConstants.longitude = it.lng.toString()
            val p = DPoint(it.lat, it.lng)
            val p2 = DPoint(mLat, mLng)
            mBinding.addressTv.text = it.address
            val distance: Float = CoordinateConverter.calculateLineDistance(p, p2)
            if (distance > 100) {
                mLng = it.lng
                mLat = it.lat
                LogUtils.i(
                    """
                    定位成功：${it.lng}
                    ${it.lat}
                    """.trimIndent()
                )
                //首页油站
                //mViewModel.getHomeOil(mLat, mLng, Constants.HUNTER_GAS_ID);
                homeOil()
            }
        })

        //首页卡片
        mViewModel.homeOilLiveData.observe(this, {
            mStoreRecordVo = it.storeRecordVo
            if (it.hasStore == 1) { //70km内有门店
                if (mList.size == 1) {
                    titles = arrayOf("油站", "车服")
                    mCarView == null
                    mCarView = View.inflate(mContext, R.layout.home_car_card_layout, null)
                    mCarCardBinding = HomeCarCardLayoutBinding.bind(mCarView)
                    mList.add(mCarView)
                    mCarView.findViewById<View>(R.id.quick_car_tv)
                        .setOnClickListener { view: View -> onViewClicked(view) }
                    mCarView.findViewById<View>(R.id.carview)
                        .setOnClickListener { view: View -> onViewClicked(view) }
                    mCarView.findViewById<View>(R.id.go_more_car_view)
                        .setOnClickListener { view: View -> onViewClicked(view) }
                    mCarView.findViewById<View>(R.id.car_go_location_view)
                        .setOnClickListener { view: View -> onViewClicked(view) }
                    mCarView.findViewById<View>(R.id.car_navigation_tv)
                        .setOnClickListener { view: View -> onViewClicked(view) }
                    mBinding.viewPager.setNoScroll(true)
                    mCommonNavigator.isAdjustMode = true
                    mCommonNavigator.notifyDataSetChanged()
                    mPagerAdapter.refreshData(titles, mList)
                    mBinding.viewPager.adapter = mPagerAdapter
                }
                if (it.nearest == 1) { //展示油站
                    mBinding.viewPager.currentItem = 0
                } else { //展示门店
                    mBinding.viewPager.currentItem = 1
                }
            } else {
                if (!PermissionUtils.isGranted(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    mCarCardBinding.carNoLocationLayout.visibility = View.VISIBLE
                    mCarCardBinding.carLayout.visibility = View.GONE
                } else {
                    titles = arrayOf("油站")
                    if (mList.size > 1) {
                        mList.removeAt(1)
                        mBinding.viewPager.removeViewAt(1)
                        mBinding.viewPager.setNoScroll(false)
                    }
                    mCommonNavigator.isAdjustMode = false
                    mCommonNavigator.notifyDataSetChanged()
                    mPagerAdapter.refreshData(titles, mList)
                    mBinding.viewPager.currentItem = 0
                }
            }
            if (it.hasStore == 1 && it.storeRecordVo != null && it.storeRecordVo.cardStoreInfoVo != null) { //最近是否有门店
                //车服卡片
                mCardStoreInfoVo = it.storeRecordVo.cardStoreInfoVo
                Glide.with(mContext).load(mCardStoreInfoVo?.storePicture).into(mCarCardBinding.carImgIv1)
                mCarCardBinding.carNameTv.text = mCardStoreInfoVo?.storeName
                mCarCardBinding.carAddressTv.text = mCardStoreInfoVo?.address
                mCarCardBinding.carTimeTv.text = "营业时间：每天" + mCardStoreInfoVo?.openStart
                    .toString() + "-" + mCardStoreInfoVo?.endStart
                mCarCardBinding.carNavigationTv.setText(
                    java.lang.String.format(
                        "%.2f",
                        mCardStoreInfoVo?.distance?.div(1000.0)
                    ).toString() + "KM"
                )
                mCarCardBinding.floatLayout.minimumHeight = QMUIDisplayHelper.dp2px(mContext, 40)
                mCarCardBinding.floatLayout.removeAllViews()
                if (mCardStoreInfoVo?.categoryNameList != null && mCardStoreInfoVo?.categoryNameList!!.size > 0) {
                    for (lab in mCardStoreInfoVo!!.categoryNameList) {
                        val textView = TextView(mContext)
                        textView.minHeight = QMUIDisplayHelper.dp2px(mContext, 19)
                        val textViewPadding: Int = QMUIDisplayHelper.dp2px(mContext, 5)
                        val textViewPadding2: Int = QMUIDisplayHelper.dp2px(mContext, 3)
                        textView.setPadding(
                            textViewPadding,
                            textViewPadding2,
                            textViewPadding,
                            textViewPadding2
                        )
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        textView.setBackgroundResource(R.drawable.shape_stroke_station_tag)
                        textView.text = lab
                        mCarCardBinding.floatLayout.addView(textView)
                    }
                    mCarCardBinding.floatLayout.visibility = View.VISIBLE
                } else {
                    mCarCardBinding.floatLayout.visibility = View.INVISIBLE
                }
                if (UserConstants.login_status) {
                    //常去车服门店
                    mViewModel.oftenCars()
                }
                mCarCardBinding.carLayout.visibility = View.VISIBLE
                mCarCardBinding.carNoLocationLayout.visibility = View.GONE
            } else {
                mCarCardBinding.carLayout.visibility = View.GONE
                mCarCardBinding.carNoLocationLayout.visibility = View.VISIBLE
            }
            if (it.stations != null && it.stations
                    .get(0) != null && it.stations[0].oilPriceList != null
            ) {
                //油站卡片
                mOilCardBinding.noLocationLayout.visibility = View.GONE
                mOilCardBinding.recommendStationLayout.visibility = View.VISIBLE
                mStationsBean = it.stations[0]
                Glide.with(mContext).load(mStationsBean.gasTypeImg)
                    .into(mOilCardBinding.oilImgIv1)
                mOilCardBinding.oilNameTv.text = mStationsBean.gasName
                mOilCardBinding.oilAddressTv.text = mStationsBean.gasAddress
                if (mStationsBean != null) {
                    mOilCardBinding.oilCurrentPriceTv.text = mStationsBean.priceYfq
                    mOilCardBinding.oilOriginalPriceTv.text = "油站价¥" + mStationsBean.priceGun
                    mOilCardBinding.oilNumTv.text = mStationsBean.oilName
                    for (i in 0 until mStationsBean.oilPriceList.size) {
                        if (mStationsBean.oilNo == java.lang.String.valueOf(
                            mStationsBean.oilPriceList[i].oilNo
                        )
                        ) {
                            mStationsBean.oilPriceList[i].isSelected = true
                            mOilNo = mStationsBean.oilPriceList[i].oilNo
                        }
                    }
                    mOilCardBinding.oilNavigationTv.text = mStationsBean.distance.toString() + "km"
                    mOilCardBinding.oilOriginalPriceTv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                    if (mStationsBean.czbLabels != null && mStationsBean.czbLabels!!.size > 0
                    ) {
                        mOilTagList = mStationsBean.czbLabels as ArrayList<OilEntity.StationsBean.CzbLabelsBean>
                        mFlexAdapter.setNewData(mOilTagList)
                        mOilCardBinding.tagRecyclerView.visibility = View.VISIBLE
                        mOilCardBinding.tagBanner.setVisibility(View.GONE)
                    } else {
                        LogUtils.e(mStationsBean.toString())
                        mOilCardBinding.tagRecyclerView.visibility = View.INVISIBLE
                        orderMsg()
                    }
                    //常去油站
                    if (UserConstants.login_status) {
                        mViewModel.oftenOils()
                    }
                    //加油任务
                    mViewModel.refuelJob()
                    mViewModel.checkDistance(mStationsBean?.gasId)
                    EventTrackingManager.instance?.tracking(
                        getBaseActivity(),
                        TrackingConstant.HOME_MAIN, "gas_id=" + mStationsBean?.gasId
                    )
                }
            }
        })

        //常去油站
        mViewModel.oftenOilLiveData.observe(this, {
            if (it != null && it.size > 0) {
                mOftenList.clear()
                mOftenList = it
                mOftenList.add(0, OfentEntity("我最近常去："))
                val flexboxLayoutManager = FlexboxLayoutManager(mContext)
                flexboxLayoutManager.flexDirection = FlexDirection.ROW
                flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
                flexboxLayoutManager.alignItems = AlignItems.FLEX_START
                mBinding.oftenOilRecyclerView.layoutManager = flexboxLayoutManager
                val oftenAdapter = HomeOftenAdapter(R.layout.adapter_often_layout, mOftenList)
                mBinding.oftenOilRecyclerView.adapter = oftenAdapter
                oftenAdapter.setOnItemClickListener { adapter, view, position ->
                    startActivity(
                        Intent(context, OilDetailsActivity::class.java)
                            .putExtra(
                                Constants.GAS_STATION_ID,
                                (adapter.getItem(position) as OfentEntity).gasId
                            )
                    )
                }
                if (mBinding.viewPager.currentItem == 0) {
                    mBinding.oftenOilRecyclerView.visibility = View.VISIBLE
                }
            } else {
                mOftenList.clear()
                mBinding.oftenOilRecyclerView.visibility = View.GONE
            }
        })

        //常去车服门店
        mViewModel.oftenCarsLiveData.observe(this, {
            if (it != null && it.size > 0) {
                mCarOftenList.clear()
                mCarOftenList = it as MutableList<OftenCarsEntity>
                val oftenCarsEntity = OftenCarsEntity()
                val cardStoreInfoVoBean: CardStoreInfoVoBean = CardStoreInfoVoBean()
                cardStoreInfoVoBean.storeName = "我最近常去："
                oftenCarsEntity.setCardStoreInfoVo(cardStoreInfoVoBean)
                mCarOftenList.add(0, oftenCarsEntity)
                val flexboxLayoutManager = FlexboxLayoutManager(mContext)
                flexboxLayoutManager.flexDirection = FlexDirection.ROW
                flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
                flexboxLayoutManager.alignItems = AlignItems.FLEX_START
                mBinding.oftenCarRecyclerView.layoutManager = flexboxLayoutManager
                val oftenAdapter =
                    HomeOftenCarsAdapter(R.layout.adapter_often_layout, mCarOftenList)
                mBinding.oftenCarRecyclerView.setAdapter(oftenAdapter)
                oftenAdapter.setOnItemClickListener { adapter, view, position ->
                    val intent1 = Intent(mContext, CarServeDetailsActivity::class.java)
                    intent1.putExtra(
                        "no",
                        mCarOftenList[position].cardStoreInfoVo.getStoreNo()
                    )
                    intent1.putExtra(
                        "distance",
                        mCarOftenList[position].cardStoreInfoVo.getDistance()
                    )
                    intent1.putExtra(
                        "channel",
                        mCarOftenList[position].cardStoreInfoVo.getChannel()
                    )
                    startActivity(intent1)
                }
                if (mBinding.viewPager.currentItem == 1) {
                    mBinding.oftenCarRecyclerView.visibility = View.VISIBLE
                }
            } else {
                mCarOftenList.clear()
                mBinding.oftenCarRecyclerView.visibility = View.GONE
            }
        })

        //菜单栏
        mViewModel.menuLiveData.observe(this, {
            if (it != null || it.size > 0) {
                menuList = it
                mHomeMenuAdapter.setNewData(it)
                mHomeMenuAdapter.notifyDataSetChanged()
                mBinding.menuRecyclerView.visibility = View.VISIBLE
            } else {
                mBinding.menuRecyclerView.visibility = View.GONE
            }
        })

        //积分豪礼
        mViewModel.productLiveData.observe(this, {
            if (it.isNotEmpty()) {
                mExchangeAdapter.setNewData(it)
            }
        })
        mViewModel.refuelOilLiveData.observe(this, {
            if (it != null) {
                mBinding.oilTaskCl.visibility = View.VISIBLE
                SpanUtils.with(mBinding.oilTaskDesc)
                    .append("每单加油")
                    .append(it.amount.toString() + "元")
                    .setForegroundColor(resources.getColor(R.color.color_3E3D))
                    .append(" 单单有好礼")
                    .create()
                mBinding.taskCouponTv1.setText(it.coupons.get(0).getInfo())
                mBinding.taskCouponTv2.setText(it.coupons.get(1).getInfo())
                mBinding.taskCouponTv3.setText(it.coupons.get(2).getInfo())
                if (it.progress >= it.coupons.get(0).getNumber()) {
                    mBinding.taskNodeIv1.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish))
                } else {
                    mBinding.taskNodeIv1.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon))
                }
                if (it.progress >= it.coupons.get(1).getNumber()) {
                    mBinding.taskNodeIv2.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish))
                } else {
                    mBinding.taskNodeIv2.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon))
                }
                if (it.progress >= it.coupons.get(2).getNumber()) {
                    mBinding.taskNodeIv3.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish))
                } else {
                    mBinding.taskNodeIv3.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon))
                }
                if (it.progress == 0) {
                    mBinding.progressIn.setVisibility(View.GONE)
                } else {
                    mBinding.progressIn.setVisibility(View.VISIBLE)
                    val layoutParams: ViewGroup.LayoutParams =
                        mBinding.progressIn.getLayoutParams()
                    if (it.progress > 0 &&
                        it.progress < it.coupons.get(0).getNumber()
                    ) {
                        layoutParams.width =
                            (mBinding.taskNodeIv1.getRight() - mBinding.taskNodeIv.getLeft()
                                    - mBinding.taskNodeIv1.getWidth() / 2) / 2
                    } else if (it.progress === it.getCoupons().get(0).getNumber()) {
                        layoutParams.width =
                            (mBinding.taskNodeIv1.getRight() - mBinding.taskNodeIv.getLeft()
                                    - mBinding.taskNodeIv1.getWidth() / 2)
                    } else if (it.progress > it.coupons.get(0).getNumber() &&
                        it.progress < it.coupons.get(1).getNumber()
                    ) {
                        layoutParams.width =
                            (3 * (mBinding.taskNodeIv2.getRight() - mBinding.taskNodeIv.getLeft()) / 4
                                    - mBinding.taskNodeIv2.getWidth() / 2)
                    } else if (it.progress === it.getCoupons().get(1).getNumber()) {
                        layoutParams.width =
                            (mBinding.taskNodeIv2.getRight() - mBinding.taskNodeIv.getLeft()
                                    - mBinding.taskNodeIv2.getWidth() / 2)
                    } else if (it.progress > it.coupons.get(1).getNumber() &&
                        it.progress < it.coupons.get(2).getNumber()
                    ) {
                        layoutParams.width =
                            (5 * (mBinding.taskNodeIv3.getRight() - mBinding.taskNodeIv.getLeft()) / 6
                                    - mBinding.taskNodeIv2.getWidth() / 2)
                    } else if (it.progress) >= it.coupons.get(2).getNumber()) {
                        layoutParams.width =
                            (mBinding.taskNodeIv3.getRight() - mBinding.taskNodeIv.getLeft()
                                    - mBinding.taskNodeIv3.getWidth() / 2)
                    } else {
                        mBinding.progressIn.setVisibility(View.GONE)
                    }
                }
                mBinding.taskNumTv1.setText(
                    "第" + it.coupons.get(0).getNumber().toString() + "单"
                )
                mBinding.taskNumTv2.setText(
                    "第" + it.coupons.get(1).getNumber().toString() + "单"
                )
                mBinding.taskNumTv3.setText(
                    "第" + it.coupons.get(2).getNumber().toString() + "单"
                )
                SpanUtils.with(mBinding.oilTaskNum)
                    .append("当前已加油")
                    .append(
                        " " + Math.max(it.progress - it.usable, 0)
                            .toString() + " "
                    )
                    .setForegroundColor(resources.getColor(R.color.color_3E3D))
                    .append("单")
                    .create()
                for (i in 0 until it.getCoupons().size()) {
                    if (0 == it.getCoupons().get(i).getStatus()) {
                        couponId =
                            java.lang.String.valueOf(it.getCoupons().get(i).getCouponId())
                        break
                    }
                }
                if (!it.isButton()) {
                    mBinding.oilTaskGet.setBackground(getResources().getDrawable(R.drawable.shape_task_unclick_14radius))
                    mBinding.oilTaskGet.setText("待领取")
                } else {
                    mBinding.oilTaskGet.setBackground(getResources().getDrawable(R.drawable.oil_task_get_icon))
                    mBinding.oilTaskGet.setText("去领取")
                }
                mBinding.oilTaskGet.setOnClickListener { view ->
                    if (!UserConstants.login_status) {
                        showToastInfo("请登录后查看")
                    } else {
                        mViewModel.receiverJobCoupon(it.getId(), couponId)
                    }
                }
                mBinding.taskRuleIv.setOnClickListener { view ->
                    if (mOilMonthRuleDialog == null) {
                        mOilMonthRuleDialog =
                            OilMonthRuleDialog(mContext, getBaseActivity(), it.url)
                        mOilMonthRuleDialog.show(view)
                    } else {
                        mOilMonthRuleDialog.show(view)
                    }
                }
            } else {
                mBinding.oilTaskCl.setVisibility(View.GONE)
            }
        })
        mViewModel.distanceLiveData.observe(this, {
            isPay = it.isPay()
            isFar = if (it.isHere) {
                false
            } else {
                true
            }
        })

        //加油任务领奖
        mViewModel.receiverCouponLiveData.observe(this, {
            showToastSuccess("领取成功~")
            mViewModel.refuelJob()
        })
        mineViewModel.userLiveData.observe(this) { data ->
            if (data.isHasBuyOldMonthCoupon()) {
                WebViewActivity.openWebActivity(
                    getActivity() as MainActivity?,
                    Constants.MARKET_ACTIVITIES_URL
                )
            } else {
                if (TextUtils.isEmpty(data.getMonthCardExpireDate())) {
                    WebViewActivity.openWebActivity(
                        getActivity() as MainActivity?,
                        Constants.BUY_MONTH_CARD_URL
                    )
                } else {
                    WebViewActivity.openWebActivity(
                        getActivity() as MainActivity?,
                        Constants.BUY_IN_ADVANCE_MONTH_CARD_URL
                    )
                }
            }
        }
    }

    private fun location() {
        mViewModel.location()
    }

    //闪屏广告页面
    private fun adInfo() {
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.APP_START_AD)
            .observe(this) { data ->
                if (data != null && data.size() > 0) {
                    val gson = Gson()
                    val bannerStr: String = gson.toJson(data.get(0))
                    UserConstants.splash_screen_ad = bannerStr
                } else {
                    UserConstants.splash_screen_ad = ""
                }
            }
    }

    private fun showNumDialog(stationsBean: OilEntity.StationsBean) {
        mOilGunPosition = -1
        //油号dialog
        mOilNumDialog = OilNumDialog(mContext, stationsBean)
        mOilNumDialog.setOnItemClickedListener(object : OnItemClickedListener() {
            fun onOilTypeClick(
                adapter: BaseQuickAdapter<*, *>, view: View?, position: Int,
                oilNumAdapter: OilNumAdapter, oilGunAdapter: OilGunAdapter
            ) {
                val data: List<OilTypeEntity> = adapter.getData()
                isShowAmount = false
                for (i in data.indices) {
                    data[i].setSelect(false)
                }
                data[position].setSelect(true)
                adapter.notifyDataSetChanged()
                for (i in 0 until mStationsBean.getOilPriceList().size()) {
                    for (j in 0 until mStationsBean.getOilPriceList().get(i).getGunNos().size()) {
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false)
                    }
                }
                val oilPriceList: List<OilEntity.StationsBean.OilPriceListBean> =
                    data[position].getOilPriceList()
                for (i in oilPriceList.indices) {
                    oilPriceList[i].setSelected(false)
                }
                oilPriceList[0].setSelected(true)
                mOilNo = oilPriceList[0].getOilNo()
                mOilGunPosition = 0
                oilNumAdapter.setNewData(oilPriceList)
                oilGunAdapter.setNewData(oilPriceList[mOilGunPosition].getGunNos())
                mOilCardBinding.oilCurrentPriceTv.setText(oilPriceList[0].getPriceYfq())
                mOilCardBinding.oilOriginalPriceTv.setText("油站价¥" + oilPriceList[0].getPriceGun())
                mOilCardBinding.oilNumTv.setText(oilPriceList[0].getOilName())
            }

            fun onOilNumClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View?,
                position: Int,
                oilGunAdapter: OilGunAdapter
            ) {
                val data: List<OilEntity.StationsBean.OilPriceListBean> = adapter.getData()
                for (i in data.indices) {
                    data[i].isSelected = false
                }
                data[position].isSelected = true
                adapter.notifyDataSetChanged()
                for (i in 0 until mStationsBean.oilPriceList.size()) {
                    for (j in 0 until mStationsBean.oilPriceList.get(i).getGunNos().size()) {
                        mStationsBean.oilPriceList.get(i).getGunNos().get(j).setSelected(false)
                    }
                }
                mOilNo = data[position].oilNo
                mOilGunPosition = 0
                isShowAmount = false
                oilGunAdapter.setNewData(data[position].gunNos)
                mOilCardBinding.oilCurrentPriceTv.setText(data[position].priceYfq)
                mOilCardBinding.oilOriginalPriceTv.setText("油站价¥" + data[position].priceGun)
                mOilCardBinding.oilNumTv.setText(data[position].oilName)
            }

            fun onOilGunClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
                val data: List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> =
                    adapter.getData()
                for (i in data.indices) {
                    data[i].isSelected = false
                }
                data[position].isSelected = true
                adapter.notifyDataSetChanged()
                mOilGunPosition = position
            }

            fun onQuickClick(
                view: View,
                oilNumAdapter: OilNumAdapter,
                oilGunAdapter: OilGunAdapter
            ) {
                if (mOilGunPosition == -1) {
                    showToastInfo("请选择枪号")
                } else {
                    for (i in 0 until oilGunAdapter.getData().size()) {
                        if (oilGunAdapter.getData().get(i).isSelected()) {
                            isShowAmount = true
                        }
                    }
                    if (isShowAmount) {
                        if (isFar) {
                            showChoiceOil(
                                oilNumAdapter,
                                oilGunAdapter,
                                mStationsBean.gasName,
                                view
                            )
                        } else {
                            EventTrackingManager.instance?.tracking(
                                getBaseActivity(),
                                TrackingConstant.GAS_GUN_NO, "type=1"
                            )
                            val intent = Intent(getContext(), OilOrderActivity::class.java)
                            intent.putExtra("stationsBean", mStationsBean as Serializable?)
                            intent.putExtra("oilNo", mOilNo)
                            intent.putExtra(
                                "gunNo",
                                oilGunAdapter.getData().get(mOilGunPosition).getGunNo()
                            )
                            startActivity(intent)
                            closeDialog()
                        }
                    } else {
                        showToastInfo("请选择枪号")
                    }
                }
            }

            fun closeAll() {
                closeDialog()
            }
        })
        mOilNumDialog.show()
    }

    private fun showFailLocation() {
        mLocationTipsDialog = LocationTipsDialog(mContext, mBinding.locationIv)
        mLocationTipsDialog.setOnClickListener { view ->
            when (view.getId()) {
                R.id.location_agin -> {
                }
                R.id.all_oil -> BusUtils.postSticky(
                    EventConstants.EVENT_CHANGE_FRAGMENT,
                    EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT)
                )
            }
        }
        mLocationTipsDialog.show()
    }

    private fun showChoiceOil(
        oilNumAdapter: OilNumAdapter,
        oilGunAdapter: OilGunAdapter,
        stationName: String,
        view: View
    ) {
        mGasStationTipsDialog =
            GasStationLocationTipsDialog(mContext, getBaseActivity(), view, stationName)
        mGasStationTipsDialog.showPayBt(isPay)
        mGasStationTipsDialog.setOnClickListener { view1 ->
            when (view1.getId()) {
                R.id.select_agin -> {
                    EventTrackingManager.getInstance().tracking(
                        getBaseActivity(),
                        TrackingConstant.GAS_FENCE, "type=1"
                    )
                    closeDialog()
                }
                R.id.navigation_tv -> if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    EventTrackingManager.getInstance().tracking(
                        getBaseActivity(),
                        TrackingConstant.GAS_FENCE, "type=2"
                    )
                    val navigationDialog = NavigationDialog(
                        getBaseActivity(),
                        mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                        mStationsBean.getGasName()
                    )
                    closeDialog()
                    navigationDialog.show()
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装")
                }
                R.id.continue_view -> {
                    isFar = false

//                            showAmountDialog(mStationsBean, oilNumAdapter.getData(),
//                                    mOilNoPosition, mOilGunPosition);
                    EventTrackingManager.getInstance().tracking(
                        getBaseActivity(),
                        TrackingConstant.GAS_FENCE, "type=3"
                    )
                    val intent = Intent(getContext(), OilOrderActivity::class.java)
                    intent.putExtra("stationsBean", mStationsBean as Serializable?)
                    intent.putExtra("oilNo", mOilNo)
                    intent.putExtra(
                        "gunNo",
                        oilGunAdapter.getData().get(mOilGunPosition).getGunNo()
                    )
                    startActivity(intent)
                    closeDialog()
                }
            }
        }
        mGasStationTipsDialog.show()
    }

    private fun showCarDialog(carServeStoreBean: CarServeStoreDetailsBean) {
        mCarServiceDialog = CarServiceDialog(mContext, carServeStoreBean)
        mCarServiceDialog.show()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {}
    override fun onRefresh(refreshLayout: RefreshLayout) {
        homeOil()
        mViewModel.oftenOils()
        mViewModel.oftenCars()
        if (mStationsBean != null) {
            mViewModel.checkDistance(mStationsBean?.gasId)
        }
        mViewModel.menu()
        mViewModel.refuelJob()
        mViewModel.homeProduct()
        loadBanner()
        refreshLayout.finishRefresh()
    }

    private fun jumpToPayResultAct(orderPayNo: String, orderNo: String) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return
        }
        val intent = Intent(mContext, RefuelingPayResultActivity::class.java)
        intent.putExtra("orderPayNo", orderPayNo)
        intent.putExtra("orderNo", orderNo)
        startActivity(intent)
        closeDialog()
    }

    private fun closeDialog() {
        if (mOilNumDialog != null) {
            mOilNumDialog.dismiss()
            mOilNumDialog == null
        }
        if (mLocationTipsDialog != null) {
            mLocationTipsDialog = null
        }
        if (mGasStationTipsDialog != null) {
            mGasStationTipsDialog = null
        }
        if (mCarServiceDialog != null) {
            mCarServiceDialog = null
        }
        isShowAmount = false
        homeOil()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.removeSticky(EventConstants.EVENT_JUMP_HUNTER_CODE)
    }

    companion object {
        val instance: HomeFragment
            get() = HomeFragment()
    }
}