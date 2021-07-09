package com.xxjy.pay

interface IPayListener {
    fun onSuccess()
    fun onFail()
    fun onCancel()
}