package com.xxjy.common.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

abstract class BaseFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ARouter.getInstance().inject(this)  // Start auto inject.
    }

    open fun getBaseActivity(): BaseActivity? {
        return if (activity is BaseActivity) {
            activity as BaseActivity?
        } else null
    }

    fun showToast(str: String?) =
        getBaseActivity()?.showToast(str)

    fun showToastInfo(str: String?) =
        getBaseActivity()?.showToastInfo(str)

    fun showToastWarning(str: String?) =
        getBaseActivity()?.showToastWarning(str)

    fun showToastError(str: String?) =
        getBaseActivity()?.showToastError(str)

    fun showToastSuccess(str: String?) =
        getBaseActivity()?.showToastSuccess(str)

}