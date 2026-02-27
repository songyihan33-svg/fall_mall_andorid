package com.example.fall_mall_andorid.data.model.home

/**
 * 首页-特惠推荐 接口返回的 result
 * GET /hot/preference
 * @see <a href="https://s.apifox.cn/b2a05857-d21a-4a59-b3f8-aaf35972c78d/api-180356233">首页-特惠推荐</a>
 */
data class HotPreferenceResult(
    val id: String,
    val title: String,
    val subTypes: List<HotPreferenceSubType>? = null
)
