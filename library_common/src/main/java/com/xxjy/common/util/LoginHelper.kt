package com.xxjy.common.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.umeng.UMengManager

/**
 * @author power
 * @date 12/4/20 3:31 PM
 * @project RunElephant
 * @description:
 */
object LoginHelper {
    var callBack: CallBack? = null
    fun login(loginCallBack: CallBack?) {
        if (!UserConstants.login_status) {
            callBack = loginCallBack
         ARouterManager.navigation(RouteConstants.Personal.A_LOGIN).navigation()
        } else {
            loginCallBack?.onLogin()
        }
    }

    fun loginOut(activity: AppCompatActivity?) {
        UserConstants.login_status=false
        UserConstants.mobile=""
        UserConstants.token=""
        UserConstants.user_type=-1
        UserConstants.openId=""
        UserConstants.new_user_red_packet=0L



        UMengManager.onProfileSignOff()
        activity?.finish()
    }

    interface CallBack {
        fun onLogin()
    }
}