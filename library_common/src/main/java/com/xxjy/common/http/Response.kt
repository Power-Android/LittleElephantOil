package com.xxjy.common.http

class Response<T> {
    var code = 0

    var msg: String? = null

    var message: String? = null

    var data: T? = null
        private set

    var isSuccess = false

    fun setData(data: T) {
        this.data = data
    }


}