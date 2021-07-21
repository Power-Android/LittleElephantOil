package com.xxjy.common.router

/**
 * 创建日期：2021/7/17 16:52
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.common.router
 * 类说明：
 */
interface RouteConstants {
    object Main {
        const val A_MAIN = "/main/MianAcivity"
    }

    object Home {
        const val F_HOME = "/home/HomeFragment"
        const val A_HOME = "/home/HomeActivity"
    }

    object CarService {
        const val F_CARSERVICE = "/carservice/CarServeFragment"
    }

    object Integral {
        const val F_INTEGRAL = "/integral/IntegralFragment"
    }

    object Oil {
        const val F_OIL = "/oil/OilFragment"
    }

    object Personal {
        const val F_MIEN = "/personal/MineFragment"
        const val A_LOGIN = "/personal/LoginActivity"
        const val A_INPUT_AUTO = "/personal/InputAutoActivity"
        const val A_MOBILE_LOGIN = "/personal/MobileLoginActivity"
        const val A_WECHAT_BINGING_PHONE = "/personal/WeChatBindingPhoneActivity"
    }

    object Web {
        const val A_WEB = "/web/WebViewActivity"
        const val A_CHAT_OR_PAY = "/web/WeChatWebPayActivity"
    }
    object ParameterKey{
        const val JUMP_STATE="jumpState"
        const val URL = "url"
    }


}