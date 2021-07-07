package com.xxjy.common.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

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