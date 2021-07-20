package com.xxjy.personal

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.Utils
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.base.BindingActivity
import com.xxjy.personal.databinding.ActivityWeChatBindingPhoneBinding
import com.xxjy.personal.viewmodel.LoginViewModel

class WeChatBindingPhoneActivity : BindingActivity<ActivityWeChatBindingPhoneBinding, LoginViewModel>() {
    private var mPhoneNumber: String? = null
    private var inviteNumber: String? = null
     override fun initView() {
        setTransparentStatusBar(mBinding.toolbar)
    }

     override fun initListener() {
        mBinding.userPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
//                if (!TextUtils.isEmpty(s)) {
//                    mBinding.userPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBig);
//                } else {
//                    mBinding.userPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSmall);
//                }
                if (s.length > 0) {
                    mBinding.registerUserClearPhone.visibility = View.VISIBLE
                } else {
                    mBinding.registerUserClearPhone.visibility = View.GONE
                }
                refreshLoginState()
            }
        })
        mBinding.close.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.registerUserClearPhone.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.loginGetCode.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.userInviteNumberLayout.setOnClickListener { view: View -> onViewClicked(view) }
         ClickUtils.applyGlobalDebouncing(mBinding.loginGetCode,this::onViewClicked)
    }

     override fun onViewClicked(view: View?) {
        when (view?.id) {
            R.id.close -> finish()
            R.id.register_user_clear_phone -> mBinding.userPhoneNumber.setText("")
            R.id.login_get_code -> {
                mPhoneNumber = mBinding.userPhoneNumber.text.toString()
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning("请填写手机号")
                    return
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning("请输入正确手机号")
                    return
                }
                inviteNumber = mBinding.invitationEt.getText().toString()
                if (!TextUtils.isEmpty(inviteNumber)) {
                    if (inviteNumber!!.length == 4 || inviteNumber!!.length == 11) {
                    } else {
                        showToastWarning("请输入正确邀请人")
                        return
                    }
                }
                autoCode
            }
            R.id.user_invite_number_layout -> if (isDown) {
                mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(90f).start()
                mBinding.invitationLl.visibility = View.GONE
                isDown = false
            } else {
                mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(0f).start()
                mBinding.invitationLl.visibility = View.VISIBLE
                isDown = true
            }
            else -> {
            }
        }
    }

     override fun dataObservable() {
        mViewModel.mCodeLiveData.observe(this) { data ->
            if (data) {
                showToastSuccess("发送成功")
                InputAutoActivity.TAG_LOGIN_PHONE_NUMBER = mPhoneNumber
                InputAutoActivity.INVITE_CODE = inviteNumber
                InputAutoActivity.openInputAutoCodeAct(this@WeChatBindingPhoneActivity)
            } else {
                showToastWarning("发送失败")
            }
        }
    }

    private val autoCode: Unit
        private get() {
            mViewModel.sendCode("10", mPhoneNumber!!)
        }

    private fun refreshLoginState() {
        val phoneNumber: String = mBinding.userPhoneNumber.getText().toString()
        mBinding.loginGetCode.isEnabled = !TextUtils.isEmpty(phoneNumber) && phoneNumber.length > 10
    }

    companion object {
        private var isDown = false
        fun openBindingWxAct(activity: BaseActivity) {
            val intent = Intent(activity, WeChatBindingPhoneActivity::class.java)
            activity.startActivity(intent)
        }
    }
}