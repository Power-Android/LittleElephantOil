package com.xxjy.pay

import java.util.*

class PayListenerUtils private constructor() {
    fun addListener(listener: IPayListener) {
        if (!resultList.contains(listener)) {
            resultList.add(listener)
        }
    }

    fun removeListener(listener: IPayListener) {
        resultList.remove(listener)
    }

    fun addSuccess() {
        for (listener in resultList) {
            listener.onSuccess()
        }
    }

    fun addCancel() {
        for (listener in resultList) {
            listener.onCancel()
        }
    }

    fun addFail() {
        for (listener in resultList) {
            listener.onFail()
        }
    }

    companion object {
        @get:Synchronized
        var instance: PayListenerUtils? = null
            get() {
                if (field == null) {
                    field = PayListenerUtils()
                }
                return field
            }
            private set
        private val resultList: ArrayList<PayListener> = ArrayList<PayListener>()
    }
}