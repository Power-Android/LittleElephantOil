package com.xxjy.web.jscalljava.jsbean

import com.blankj.utilcode.util.GsonUtils

class ToolBarStateBean {
    var toolBarBgColor: String? = null
    var toolBarTitleColor: String? = null

    companion object {
        fun parseFromString(beanJson: String?): ToolBarStateBean? {
            return try {
                GsonUtils.fromJson(beanJson, ToolBarStateBean::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}