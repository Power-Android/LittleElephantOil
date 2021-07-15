package com.xxjy.shumei

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import com.ishumei.smantifraud.SmAntiFraud

object SmAntiFraudManager {
    fun initSdk(context: Context) {
// 如果AndroidManifest.xml中没有指定主进程名字，主进程名默认与packagename相同
// 如下条件判断保证只在主进程中初始化SDK
        if (context.packageName == getCurProcessName(context)) {
            val option: SmAntiFraud.SmOption = SmAntiFraud.SmOption()
            //1.通用配置项
            option.setOrganization("C75pvD26pNcSgneaKmEF") //必填，组织标识，邮件中organization项
            option.setAppId("default") //必填，应用标识，登录数美后台应用管理查看
            option.setPublicKey(
                "MIIDLzCCAhegAwIBAgIBMDANBgkqhkiG9w0BAQUFADAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wHhcNMjEwNDI5MDcwMDEzWhcNNDEwNDI0MDcwMDEzWjAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCVPLe8qfKNK5XH7LSi+eUH9T1fW/UXM2bkAdTAF9nn2Ix810mTQ2EBPSpn1+kTyidB1Bxgb8exs39FkmbwdytIfSAlfskCIk20REj/3FYjyJf8LSKWwIZzsFVY3o0G12ras6suxjHv2A1zmAqh1yQHaOo3vqos3OZYcEHHKv86z4XR1S/jy9jOKyEuByX7ZbplBdClpyUxMoRxE9rMXukIj+opyqcNcrbYHnSt2OnXyK/YewcevB8Rk4CR0gH/CWl9E1bilFbuFkEeobRPPtlxTqUoavdBFlqB00Ap0JEA2HHb9zoo5mCH8a/7wnDEpBTQqIdBtA8XASXL0jmtusTvAgMBAAGjUDBOMB0GA1UdDgQWBBTfAEVb1gJPJ7SL4rlxZus0CmjaTTAfBgNVHSMEGDAWgBTfAEVb1gJPJ7SL4rlxZus0CmjaTTAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4IBAQATRjFpTJzdN86ixtnEsAcK+LpkF0b+OZSQUqYe0Z5Mmcga24s5rtIquNIQ5SUWwVHZJ4mKl5RpTM3vYmdix6Uv1yXdvlsRwj12tKGPPybMrFXzWYagr4CCXKI6O0UJMbcmooK1r08v9iU1cYiCaYudKjK81X7wgbjpSBIr76aA8oWoqW/cGdCNqh4dcWBdhADvj7hUDgNJyCmX6xhmjZX45sXzxolFn5GY/+L9ZJfAEzytwntKYdR3y+kE9F+KfBgmc1iQE8mnAs0k7VvEKodsSuUQAx4DJ/ndYxZQ8D9mGp2m5RHVN8EgpURcEfyihHYklKNqDjONxZ6m2RavzZtF"
            ) //必填，加密KEY，邮件中android_public_key附件内容
            //            option.setAinfoKey("BupPymBCCuAEchSGAEhhfCPINsDlgfjoKnvPwvlTFWZsCIOUaDtBJGrReVigSijd");   //必填，加密KEY，邮件中Android ainfo key项

// 2连接机房特殊配置项
//
// 2.1业务机房在国内
// 1) 用户分布：中国（默认设置）
// option.setArea(SmAntiFraud.AREA_BJ);
// 2) 用户分布：全球
// option.setArea(SmAntiFraud.AREA_BJ);
// 注意，此处 host 必须添加协议头（https:// 或 http://），下同。
// String host = "http://fp-it-acc.fengkongcloud.com";
// option.setUrl(host + "/v3/profile/android");
// option.setConfUrl(host + "/v3/cloudconf");
//
// 2.2业务机房在欧美
// 1) 用户分布：欧美
// option.setArea(SmAntiFraud.AREA_FJNY);
// 2) 用户分布：全球
// option.setArea(SmAntiFraud.AREA_FJNY);
// String host = "http://fp-na-it-acc.fengkongcloud.com";
// option.setUrl(host + "/v3/profile/android");
// option.setConfUrl(host + "/v3/cloudconf");
//
// 2.3业务机房在东南亚
// 1) 用户分布：东南亚
// option.setArea(SmAntiFraud.AREA_XJP)
// 2) 用户分布：全球
// option.setArea(SmAntiFraud.AREA_XJP);
// String host = "http://fp-sa-it-acc.fengkongcloud.com";
// option.setUrl(host + "/v3/profile/android");
// option.setConfUrl(host + "/v3/cloudconf");

// 2.4 私有化特殊配置
// 1) 设置 area
// option.setArea("xxxxxx");  //  必填，组织标识，邮件中organization项
// 2) 设置私有地址
// String host = "https://private-host"; // 将 private-host 替换为您自己的主机名（域名）
// option.setUrl(host + "/v3/profile/android");
// option.setTraceUrl(host + "/v3/tracker?os=android");
// option.setTraceUrl(host + "/v3/tracker?os=android");

//3.SDK初始化
            SmAntiFraud.create(context, option)

//4.获取设备标识，注意获取deviceId，这个接口在需要使用deviceId时地方调用
//            String deviceId = SmAntiFraud.getDeviceId();
        }
    }

    private fun getCurProcessName(context: Context): String? {
        val pid = Process.myPid()
        val mActivityManager: ActivityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager
            .runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

    fun getDeviceId(): String? = try {
        SmAntiFraud.getDeviceId()
    } catch (e: Exception) {
        null
    }
}