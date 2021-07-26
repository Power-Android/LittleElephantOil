package com.xxjy.common.entity

data class CarServeStoreDetailsBean (
    var cardStoreInfoVo: CardStoreInfoVoBean,
    var productCategory: Map<String, List<CarServeProductsBean>>
)