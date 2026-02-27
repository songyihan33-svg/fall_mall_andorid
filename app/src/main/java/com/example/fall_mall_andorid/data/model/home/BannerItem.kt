package com.example.fall_mall_andorid.data.model.home

/**
 * 首页 Banner 单项
 * 对应接口：GET /home/banner 的 result 数组元素
 * @see <a href="https://s.apifox.cn/b2a05857-d21a-4a59-b3f8-aaf35972c78d/api-180356226">首页-Banner</a>
 */
data class BannerItem(
    val id: String,
    val imgUrl: String,
    val hrefUrl: String,
    val type: String
)