package com.xxjy.jyyh

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.BusUtils.Bus
import com.xxjy.carservice.CarServeFragment
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.BannerPositionConstants
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.dialog.HomeAdDialog
import com.xxjy.common.dialog.HomeNewUserDialog
import com.xxjy.common.dialog.NoticeTipsDialog
import com.xxjy.common.dialog.VersionUpDialog
import com.xxjy.common.entity.EventEntity
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.util.AppManager
import com.xxjy.common.util.NotificationsUtils
import com.xxjy.common.util.Util
import com.xxjy.common.util.eventtrackingmanager.EventTrackingManager
import com.xxjy.common.util.eventtrackingmanager.TrackingConstant
import com.xxjy.common.util.eventtrackingmanager.TrackingEventConstant
import com.xxjy.common.vm.BannerViewModel
import com.xxjy.home.HomeFragment
import com.xxjy.integral.IntegralFragment
import com.xxjy.jyyh.databinding.ActivityMainBinding
import com.xxjy.oil.OilFragment
import com.xxjy.personal.MineFragment
import com.xxjy.personal.viewmodel.AboutUsViewModel
import com.xxjy.push.JPushManager
import com.xxjy.shanyan.ShanYanManager
import com.xxjy.shumei.SmAntiFraudManager
import com.xxjy.umeng.UMengManager

@Route(path = RouteConstants.Main.A_MAIN)
class MainActivity : BindingActivity<ActivityMainBinding, MainViewModel>() {
    private var mLastFgIndex = -1
    private var clickTime: Long = 0
    private var mHomeFragment: HomeFragment? = null
    private var mOilFragment: OilFragment? = null
    private var mCarServeFragment: CarServeFragment? = null
    private var mIntergralFragment: IntegralFragment? = null
    private var mMineFragment: MineFragment? = null
    private var mTransaction: FragmentTransaction? = null
    private var isNewUser = -1 //新用户，未满3单跳油站列表，老用户满3单跳首页

    private val aboutUsViewModel: AboutUsViewModel by viewModels()
    private val bannerViewModel: BannerViewModel by viewModels()

    @Bus(tag = EventConstants.EVENT_CHANGE_FRAGMENT, sticky = true)
    fun onEvent(event: EventEntity) {
        if (TextUtils.equals(event.event, EventConstants.EVENT_CHANGE_FRAGMENT)) {
            TrackingConstant.OIL_MAIN_TYPE = "1"
            mBinding.navView.selectedItemId = R.id.navigation_oil
        } else if (TextUtils.equals(event.event, EventConstants.EVENT_TO_INTEGRAL_FRAGMENT)) {
            mBinding.navView.selectedItemId = R.id.navigation_integral
        } else if (TextUtils.equals(event.event, EventConstants.EVENT_TO_OIL_FRAGMENT)) {
            mBinding.navView.selectedItemId = R.id.navigation_oil
        } else if (TextUtils.equals(event.event, EventConstants.EVENT_TO_CAR_FRAGMENT)) {
            mBinding.navView.selectedItemId = R.id.navigation_car_serve
        }

    }

    private fun initSdk() {
        Thread {
            appChannel = UserConstants.app_channel_key
            if (TextUtils.isEmpty(appChannel)) {
                appChannel = AppManager.appMetaChannel.toString()
                UserConstants.app_channel_key = appChannel
            }
            //初始化闪验sdk
            ShanYanManager.initShanYnaSdk(this)
            //极光推送配置
            JPushManager.initJPush(this)
            //友盟统计
            UMengManager.init(this)
            //数美风控
            SmAntiFraudManager.initSdk(this)
        }.start()
    }

    override fun initView() {
        initSdk()
        BusUtils.register(this)
        mHomeFragment = null
        mOilFragment = null
        mCarServeFragment = null
        mIntergralFragment = null
        mMineFragment = null
        disPathIntentMessage(intent)

        //积分权益隐藏判断
        mViewModel.osOver().observe(this, {
            if (!it) {
                UserConstants.gone_integral = true
                mBinding.navView.menu.removeItem(R.id.navigation_home)
                mBinding.navView.menu.removeItem(R.id.navigation_integral)
            } else {
                UserConstants.gone_integral = false
            }
        })

        initNavigationView()
        val state: Int = intent.getIntExtra("jumpState", -1)
        showDiffFragment(state)
        if (state == -1) {
            checkVersion()
            //新老用户展示tab判断 新客户展示油站列表
            mViewModel.isNewUser().observe(this) {
                isNewUser = if (it) {
                    1
                } else {
                    if (UserConstants.gone_integral) {
                        1
                    } else {
                        0
                    }
                }
                if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) { //猎人码绑定油站
                    showDiffFragment(isNewUser)
                }
            }
        } else {
            showNewUserDialog()
        }
        UserConstants.notification_remind_user_center =
            !NotificationsUtils.isNotificationEnabled(this)
    }

    private fun showDiffFragment(position: Int) {
        if (position != -1) {
            showFragment(position)
            when (position) {
                0 -> mBinding.navView.selectedItemId = R.id.navigation_home
                1 -> mBinding.navView.selectedItemId = R.id.navigation_oil
                2 -> mBinding.navView.selectedItemId = R.id.navigation_car_serve
                3 -> mBinding.navView.selectedItemId = R.id.navigation_integral
                4 -> mBinding.navView.selectedItemId = R.id.navigation_mine
            }
        }
    }

    private fun initNavigationView() {
        mBinding.navView.itemIconTintList = null
        mBinding.navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> showFragment(Constants.TYPE_HOME)
                R.id.navigation_oil -> showFragment(Constants.TYPE_OIL)
                R.id.navigation_car_serve -> {
                    showFragment(Constants.TYPE_CAR_SERVE)
                    EventTrackingManager.instance?.trackingEvent(
                        this,
                        TrackingConstant.CF_PAGE_HOME,
                        TrackingEventConstant.CF_EVENT_ICON
                    )
                }
                R.id.navigation_integral -> showFragment(Constants.TYPE_INTEGRAL)
                R.id.navigation_mine -> {
                    showFragment(Constants.TYPE_MINE)
                    EventTrackingManager.instance?.trackingEvent(
                        this,
                        TrackingConstant.HOME_MINE,
                        TrackingEventConstant.EVENT_HOME_MINE
                    )
                }
            }
            true
        }
        mBinding.navView.selectedItemId = R.id.navigation_home
    }

    private fun showFragment(index: Int) {
        mTransaction = supportFragmentManager.beginTransaction()
        hideFragment(mTransaction)
        mLastFgIndex = index
        when (index) {
            Constants.TYPE_HOME -> {
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment()
                    mHomeFragment?.let { mTransaction!!.add(R.id.fragment_group, it) }
                }
                mHomeFragment?.let { mTransaction!!.show(it) }
            }
            Constants.TYPE_OIL -> {
                if (mOilFragment == null) {
                    mOilFragment = OilFragment()
                    mOilFragment?.let { mTransaction!!.add(R.id.fragment_group, it) }
                }
                mOilFragment?.let { mTransaction!!.show(it) }
            }
            Constants.TYPE_CAR_SERVE -> {
                if (mCarServeFragment == null) {
                    mCarServeFragment = CarServeFragment()
                    mCarServeFragment?.let { mTransaction!!.add(R.id.fragment_group, it) }
                }
                mCarServeFragment?.let { mTransaction!!.show(it) }
            }
            Constants.TYPE_INTEGRAL -> {
                if (mIntergralFragment == null) {
                    mIntergralFragment = IntegralFragment()
                    mIntergralFragment?.let { mTransaction!!.add(R.id.fragment_group, it) }
                }
                mIntergralFragment?.let { mTransaction!!.show(it) }
            }
            Constants.TYPE_MINE -> {
                if (mMineFragment == null) {
                    mMineFragment = MineFragment()
                    mMineFragment?.let { mTransaction!!.add(R.id.fragment_group, it) }
                }
                mMineFragment?.let { mTransaction!!.show(it) }
            }
        }
        mTransaction!!.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction?) {
        when (mLastFgIndex) {
            Constants.TYPE_HOME -> if (mHomeFragment != null) {
                transaction!!.hide(mHomeFragment!!)
            }
            Constants.TYPE_OIL -> if (mOilFragment != null) {
                transaction!!.hide(mOilFragment!!)
            }
            Constants.TYPE_CAR_SERVE -> if (mCarServeFragment != null) {
                transaction!!.hide(mCarServeFragment!!)
            }
            Constants.TYPE_INTEGRAL -> if (mIntergralFragment != null) {
                transaction!!.hide(mIntergralFragment!!)
            }
            Constants.TYPE_MINE -> if (mMineFragment != null) {
                transaction!!.hide(mMineFragment!!)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(Constants.CURRENT_FRAGMENT_KEY, mCurrentFgIndex);
    }

    /**
     * 处理回退事件
     */
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - clickTime > Constants.DOUBLE_INTERVAL_TIME) {
            showToastInfo(getString(R.string.double_click_exit_toast))
            clickTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }

    override fun initListener() {}
    override fun onViewClicked(view: View?) {}
    override fun dataObservable() {
        aboutUsViewModel.checkVersionLiveData.observe(this) {
            val compare: Int = Util.compareVersion(it.lastVersion, Util.versionName)
            if (compare == 1) {
                //是否强制更新，0：否，1：是
                val checkVersionDialog = VersionUpDialog(this, it, true)
                checkVersionDialog.setOnDismissListener { newUserStatus() }
                checkVersionDialog.show()
            } else {
                newUserStatus()
            }
        }
    }

    private fun adData() {
        bannerViewModel.bannerOfPosition(BannerPositionConstants.APP_OPEN_AD)
            .observe(this) {
                if (it != null && it.isNotEmpty()) {
                    val homeAdDialog = HomeAdDialog(this, it[0])
                    homeAdDialog.show(mBinding.root)
                    homeAdDialog.setOnItemClickedListener { showNotification() }
                } else {
                    showNotification()
                }
            }
    }

    //新人礼包
    private fun newUserStatus() {
        if (UserConstants.login_status) {
            mViewModel.newUserStatus()
                .observe(this) {
                    if (it != null && it.status == 1) {
                        val homeNewUserDialog: HomeNewUserDialog = HomeNewUserDialog.instance
                        homeNewUserDialog.setData(this, it)
                        homeNewUserDialog.show(mBinding.root)
                        homeNewUserDialog.setOnItemClickedListener { adData() }
                    } else {
                        adData()
                    }
                }
        } else {
            adData()
        }
    }

    private fun showNotification() {
        if (!UserConstants.notification_remind_version) {
            if (!UserConstants.notification_remind) {
                if (!NotificationsUtils.isNotificationEnabled(this)) {
                    val noticeTipsDialog = NoticeTipsDialog(this)
                    noticeTipsDialog.show(mBinding.fragmentGroup)
                    noticeTipsDialog.setOnItemClickedListener(object :
                        NoticeTipsDialog.OnItemClickedListener {
                        override fun onQueryClick() {
                            NotificationsUtils.requestNotify(this@MainActivity)
                        }

                        override fun onNoOpen() {
                            UserConstants.notification_remind_version = true
                        }
                    })
                    UserConstants.notification_remind = true
                }
            }
        }
    }

    private fun showNewUserDialog() {
        if (UserConstants.login_status) {
            mViewModel.newUserStatus().observe(this) {
                if (it != null && it.status == 1) {
                    val homeNewUserDialog: HomeNewUserDialog = HomeNewUserDialog.instance
                    homeNewUserDialog.setData(this, it)
                    homeNewUserDialog.show(mBinding.root)
                    homeNewUserDialog.setOnItemClickedListener { adData() }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        disPathIntentMessage(intent)
        jump(intent)
    }

    override fun onRestart() {
        super.onRestart()
        showNewUserDialog()
    }

    private fun jump(intent: Intent) {
        val state = intent.getIntExtra("jumpState", -1)
        showDiffFragment(state)
    }

    private fun disPathIntentMessage(intent: Intent) {
        var intent: Intent? = intent
        if (intent == null) {
            intent = getIntent()
        }
        val startFrom = intent!!.getIntExtra("startFrom", 0)
        UserConstants.startFrom = startFrom.toString()
        val intentInfo = intent.getStringExtra(TAG_FLAG_INTENT_VALUE_INFO)
        if (TextUtils.isEmpty(intentInfo)) return
            //TODO LATE
//        NaviActivityInfo.disPathIntentFromUrl(this, intentInfo)
        intent.removeExtra(TAG_FLAG_INTENT_VALUE_INFO)
    }

    private fun checkVersion() {
        aboutUsViewModel.checkVersion()
    }

    companion object {
        private var appChannel = "" //标记app的渠道

        //极光intent
        const val TAG_FLAG_INTENT_VALUE_INFO = "intentInfo"

        /**
         * 打开首页并清空栈
         *
         * @param activity
         */
        fun openMainActAndClearTask(activity: BaseActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            //        ActivityUtils.startHomeActivity();
        }

        /**
         * 打开首页并清空栈
         *
         * @param activity
         */
        fun openMainActAndClearTaskJump(activity: BaseActivity, jumpCode: Int) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("jumpState", jumpCode)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            //        ActivityUtils.startHomeActivity();
        }
    }
}