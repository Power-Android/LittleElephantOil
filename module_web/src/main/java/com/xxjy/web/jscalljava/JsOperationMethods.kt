package com.xxjy.web.jscalljava

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2018/5/13.
 * 添加供给 js 的调用方法接口 ,为了方便统计,在这里抽取 方法
 *
 *
 * 已经实现的方法有
 * 1, [.getAppInfo]                 获取app信息
 * 3, [.startShare]           调起分享
 * 4, [.showSharedIcon]       显示分享图标并传递分享内容
 * 5, [.showHelpIcon]         显示客服图标并传递手机号
 * 6, [.toRechargeOneCard]          跳转app内部充值1号卡界面
 * 7, [.dialPhone]            跳转拨打电话界面
 * 8, [.toLogin]                    跳转登录界面
 * 9, [.toWechatPay]          跳转微信支付界面
 * 9, [.toAppletPay]          跳转微信小程序支付界面
 * 10, [.toAccountDetails]   账户明细
 * 11, [.nativeActivity]      跳转app内部本地界面
 * 12, [.toAliPay]            调起支付宝支付
 */
interface JsOperationMethods {
    /**
     * 获取app 的信息
     */
    fun getAppInfo(): String?

    /**
     * 调起分享
     *
     * @param shardInfo 分享的json信息
     */
    fun startShare(shardInfo: String?)

    /**
     * 展示分享图标并传入分享内容
     *
     * @param shardInfo
     */
    fun showSharedIcon(shardInfo: String?)

    /**
     * 隐藏分享图标并传入分享内容
     */
    fun hideSharedIcon()

    /**
     * 显示客服图标并传递手机号
     *
     * @param phoneNumber
     */
    fun showHelpIcon(phoneNumber: String?)

    /**
     * 跳转跳转app内部充值1号卡
     */
    fun toRechargeOneCard()

    /**
     * 调起拨打手机界面
     *
     * @param phoneNumber
     */
    fun dialPhone(phoneNumber: String?)

    /**
     * 跳转登录界面
     */
    fun toLogin()

    /**
     * 打开微信支付处理
     *
     * @param url 后台返回的js文本
     */
    fun toWechatPay(url: String?)

    /**
     * 打开微信小程序支付处理
     *
     * @param params 小程序的参数
     */
    fun toAppletPay(params: String?)

    /**
     * 账户明细
     *
     * @param checkPosition 默认选中的item
     */
    fun toAccountDetails(checkPosition: String?)

    /**
     * 跳转本地的界面
     *
     * @param intentInfo
     */
    fun nativeActivity(intentInfo: String?)

    /**
     * 调起支付宝支付
     *
     * @param intentInfo
     */
    fun toAliPay(intentInfo: String?)

    /**
     * 将toolbar状态栏的背景修改成bgcolor
     *
     * @param bgColor
     */
    fun changeToolBarState(bgColor: String?)

    /**
     * 改变toolbar状态栏至默认状态
     */
    fun changeToolBarDefault()

    /**
     * 跳转小程序购买权益卡
     */
    fun goWeChatBuyEquityCard(parameter: String?)

    /**
     * 保存图片
     * @param data
     */
    fun saveImage(data: String?)

    /**
     * 是否显示搜索
     * @param isShow
     */
    fun showSearch(isShow: Boolean)

    /**
     * 标题栏显示
     * @param isShow
     */
    fun showToolbar(isShow: Boolean)

    /**
     * 业绩中心
     * @param isShow
     */
    fun showPerformanceCenterView(isShow: Boolean)

    /**
     * 返回
     */
    fun goBack()

    /**
     * 获取状态栏
     * @return
     */
    fun getStatusBarHeight(): Int

    /**
     * 是否显示沉浸式头部
     * @param isShow
     */
    fun showImmersiveToolBar(isShow: Boolean)

    /**
     * 提现明细
     * @param isShow
     */
    fun showWithdrawalDetailsView(isShow: Boolean)

    /**
     * 修改状态栏颜色
     * @param isDefault
     * @param bgColor
     */
    fun changeToolBarState(isDefault: Boolean, bgColor: String?)

    /**
     * 跳转到微信
     */
    fun goWeChat()

    /**
     * 关闭页面
     */
    fun finishActivity()

    /**
     * 分享图片到微信
     */
    fun shareImageToWeChat(data: String?)

    /**
     * 加油省钱页
     */
    fun toRefuellingPage()

    /**
     * 首页
     */
    fun toHomePage()

    /**
     * 车服首页
     */
    fun toCarServePage()

    /**
     * 商城首页
     */
    fun toIntegralHomePage()

    /**
     * 获取openid
     * @return
     */
    fun getOpenId()

    /**
     * 埋点信息
     * @return
     */
    fun getEventTracking(): String?

    /**
     * 去优惠券列表
     */
    fun goCouponListPage()

    /**
     * 邀请好友登录
     */
    fun toLoginByInviteFriends()

    /**
     * 我的页面
     */
    fun toMyPage()

    /**
     * 去权益订单列表
     */
    fun toEquityOrderListPage()

    /**
     * 客服弹窗
     *
     */
    fun showCustomerService()

    /**
     * 导航
     */
    fun toNavigation(longitude: String?, latitude: String?, destination: String?)
}