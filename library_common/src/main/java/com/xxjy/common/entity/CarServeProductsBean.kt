package com.xxjy.common.entity

import java.io.Serializable

data class CarServeProductsBean(
    var id: Long,
    var name: String,
    var categoryId: Int,
    var childCategoryId: Int,
    var storeNo: String,
    var productNo: String,
    var channel: Int,
    var linePrice: String,
    var salePrice: String,
    var supplierPrice: String,
    var cover: String,
    var detailPic: String,
    var sequence: String,
    var status: Int,
    var saleNum: String,
    var description: String,
    var createTime: String,
    var updateTime: String,
    var deleteTime: String,
    var operator: String,
    var deletedStatus: String,
    var categoryName: String,
    var productAttribute: CarServeProductAttributeBean,
    var isSelect: Boolean = false
)
