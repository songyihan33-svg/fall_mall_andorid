package com.example.fall_mall_andorid.data.model.home

/**
 * 特惠推荐子类型下的分页商品列表
 */
data class HotPreferenceGoodsItems(
    val counts: Int,
    val pageSize: Int,
    val pages: Int,
    val page: Int,
    val items: List<CategoryGoodsItem>? = null
)
