package com.xxjy.common.util.sp

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.xxjy.common.util.sp.MContext.init

/**
 * 创建日期：2021/7/14 11:27
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.common.util.sp
 * 类说明：
 */
internal class ContextProvider : ContentProvider(){

    override fun onCreate(): Boolean {
        init(context!!)
        return true
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}