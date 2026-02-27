package com.example.fall_mall_andorid.data.model.home

/**
 * 特惠推荐下的子类型（如「精选全攻略」等）
 */
data class HotPreferenceSubType(
    val id: String,
    val title: String,
    val goodsItems: HotPreferenceGoodsItems? = null
)
