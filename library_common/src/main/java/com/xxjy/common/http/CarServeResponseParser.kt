package com.xxjy.common.http

import com.xxjy.common.provide.MContext.context
import com.xxjy.common.util.toastlib.MyToast
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.AbstractParser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import java.io.IOException
import java.lang.reflect.Type



/**
 * Response<T> 数据解析器,解析完成对Response对象做判断,如果ok,返回数据 T
 * User: ljx
 * Date: 2018/10/23
 * Time: 13:49
</T> */
@Parser(name = "CarServeResponse")
open class CarServeResponseParser<T> : AbstractParser<T?> {
    protected constructor() : super() {}
    constructor(type: Type?) : super(type!!) {}

    @Throws(IOException::class)
    override fun onParse(response: okhttp3.Response): T {
        val type: Type = ParameterizedTypeImpl[Response::class.java, mType] //获取泛型类型
        val data: Response<T> = convert(response, type)
        var t: T? = data.data //获取data字段
        if (t == null) {
            /*
             * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            if (data.code == 200) {
                data.message = "暂无数据"
            }
            t = data.message as T
        }
        if (data.code != 200) {
            MyToast.showWarning(
                context(),
                if (data.message != null) data.message else "网络请求错误"
            )
            throw ParseException(data.code.toString(), data.message, response)
        }
        return t!!
    }
}