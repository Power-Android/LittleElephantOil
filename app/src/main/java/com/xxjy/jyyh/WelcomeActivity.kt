package com.xxjy.jyyh

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.entity.BannerBean
import com.xxjy.common.util.GlideUtils
import com.xxjy.common.weight.MyCountDownTime
import com.xxjy.jyyh.databinding.ActivityWelcomeBinding
import com.xxjy.navigation.MapLocationHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class WelcomeActivity : BaseActivity(),
    PermissionUtils.SingleCallback {
    private var mWelcomeAd: ImageView? = null
    private var mWelcomeAdTv: TextView? = null
    private val isFirstIn = true
    private var isAdClick = false //是否是ad点击
    private var isShowAd = false //是否展示ad广告
    private var isDisPathInfo = false //是否已经处理信息了
    private var mAdImageUrl: String? = null
    private var mAdLinkInfo //跳转信息
            : String? = null
    private var isShownYSXY = false //是否展示过隐私协议
    private var mAlphaAnimation: AlphaAnimation? = null
    private var mObservable: Observable<Int>? = null
    private var mEmitter: ObservableEmitter<Int>? = null
    private var mCountDownTime: MyCountDownTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initView();
    }
    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

      fun initView() {
        BarUtils.setNavBarVisibility(this, false)
        mWelcomeAd = findViewById(R.id.welcome_ad) as ImageView?
        mWelcomeAdTv = findViewById(R.id.welcome_ad_tv) as TextView?
        val startFrom: Int = getIntent().getIntExtra("startFrom", 0)
        UserConstants.startFrom=startFrom.toString()
        mAlphaAnimation =
            AnimationUtils.loadAnimation(this, R.anim.alpha_welcome_ad_in) as AlphaAnimation
        mCountDownTime = MyCountDownTime.getInstence(3 * 1000, 1000)
        mCountDownTime?.setOnTimeCountDownListener(object :
            MyCountDownTime.OnTimeCountDownListener {
            override fun onTick(millisUntilFinished: Long) {
                mWelcomeAdTv?.text="跳过 (${(millisUntilFinished / 1000 + 1)}秒)"
            }

            override fun onFinish() {
                toMainActivity()
            }
        })
        isShownYSXY = UserConstants.agree_privacy
        if (isShownYSXY) {
            initAD()
        } else {
            showYSXYDialog()
        }
        mObservable = Observable.create { emitter ->
            mEmitter = emitter
            //                emitter.onComplete();
        }
        mObservable!!.subscribeOn(Schedulers.trampoline())
            .observeOn(AndroidSchedulers.mainThread()) //observeOn创建产生的实例类型ObservableObserveOn<T>
            .subscribe(object : Observer<Int?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(s: Int?) {
                    when (s) {
                        MSG_WHAT_DISPATH_INFO -> if (!isDisPathInfo) {
                            isDisPathInfo = true
                            if (isAdClick) {
                                if (!TextUtils.isEmpty(mAdLinkInfo)) {
                                    val intent =
                                        Intent(this@WelcomeActivity, MainActivity::class.java)
                                    intent.putExtra(
                                        MainActivity.TAG_FLAG_INTENT_VALUE_INFO,
                                        mAdLinkInfo
                                    )
                                    startActivity(intent)
                                } else {
                                    toMainActivity()
                                }
                            } else {
                                toMainActivity()
                            }
                        }
                        MSG_WHAT_TRY_SHOW_GUIDE -> {
                            val link: String? = intent.getStringExtra(TYPE_ACT_LINK)
                            if (!TextUtils.isEmpty(link)) {     //如果是极光推送打开的app优先展示消息,不展示广告
                                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                                if ("msg" == link) {
//                                    intent.putExtra(
//                                        MainActivity.TAG_FLAG_INTENT_VALUE_INFO,
//                                        NaviActivityInfo.NATIVE_TO_MSG_CENTER
//                                    )
                                } else {
                                    intent.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, link)
                                }
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            } else {
                                isShowAd = !TextUtils.isEmpty(mAdImageUrl)
                                if (isShowAd) {
                                    LogUtils.e("isShowAd", TimeUtils.getNowString())
                                    mEmitter?.onNext(MSG_WHAT_TRY_SHOW_AD)
                                } else {
                                    mEmitter?.onNext(MSG_WHAT_DISPATH_INFO)
                                }
                            }
                        }
                        MSG_WHAT_TRY_SHOW_AD -> initAD()
                    }
                }

                override fun onError(e: Throwable) {
                    toMainActivity()
                }

                override fun onComplete() {
                    toMainActivity()
                }
            })
        mWelcomeAd!!.setOnClickListener {
            if (isShowAd) {
                isAdClick = true
                mEmitter?.onNext(MSG_WHAT_DISPATH_INFO)
            }
        }
        mWelcomeAdTv?.setOnClickListener(View.OnClickListener { mEmitter?.onNext(MSG_WHAT_DISPATH_INFO) })
    }

    private fun showYSXYDialog() {
//        val dialog = PrivacyAgreementDialog(this)
//        dialog.getCancel().setOnClickListener(View.OnClickListener {
//            dialog.dismiss()
//            AppUtils.exitApp()
//        })
//        dialog.getConfirm().setOnClickListener(View.OnClickListener {
//            dialog.dismiss()
//            UserConstants.agree_privacy=true
//            isShownYSXY = true
//            requestPermission()
//        })
//        dialog.show()
    }

    private fun initAD() {
        val json: String = UserConstants.splash_screen_ad
        if (TextUtils.isEmpty(json)) {
//            mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
            toMainActivity()
        } else {

            val bannerBean: BannerBean = GsonUtils.fromJson(json, BannerBean::class.java)
            if (bannerBean != null) {
                loadBanner(bannerBean)
            } else {
                toMainActivity()
                //                mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
            }
        }
    }

    private fun loadBanner(bannerBean: BannerBean?) {
        if (bannerBean != null && TimeUtils.getNowMills() > TimeUtils.string2Date(bannerBean.startTime).time
            && TimeUtils.getNowMills() < TimeUtils.string2Date(bannerBean.endTime).time) {
            mAdImageUrl = bannerBean.imgUrl
            mAdLinkInfo = bannerBean.link
            GlideUtils.loadImage(this@WelcomeActivity, mAdImageUrl, mWelcomeAd)
            mWelcomeAdTv?.visibility = View.VISIBLE
            mWelcomeAdTv?.startAnimation(mAlphaAnimation)
            mCountDownTime?.start()
        } else {
            mEmitter?.onNext(MSG_WHAT_DISPATH_INFO)
        }
    }

    override  fun onStart() {
        super.onStart()
        if (isShownYSXY) {
//            requestPermission();
            next()
        }
    }

    /**
     * 请求权限
     */
    private fun requestPermission() {
        PermissionUtils.permission(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
            .callback(this)
            .request()
    }

    private operator fun next() {
        MapLocationHelper.refreshLocation(this)
        mEmitter?.onNext(MSG_WHAT_TRY_SHOW_GUIDE)
    }

    override fun onDestroy() {
        if (mCountDownTime != null) {
            mCountDownTime?.cancel()
            mCountDownTime = null
        }
        super.onDestroy()
    }

    override fun callback(
        isAllGranted: Boolean,
        granted: List<String>,
        deniedForever: List<String>,
        denied: List<String>
    ) {
        next()
    }

    companion object {
        private const val BAR_TITLE = "启动页"
        const val PERMISSION_REQUEST_CODE = 1
        private const val MSG_WHAT_DISPATH_INFO = 0 //分发信息
        private const val MSG_WHAT_TRY_SHOW_GUIDE = 1 //尝试展示引导
        private const val MSG_WHAT_TRY_SHOW_AD = 2 //尝试展示广告
        const val TYPE_ACT_LINK = "act" //打开该界面的tag
    }
}