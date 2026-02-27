package com.example.fall_mall_andorid.data.model.home

/**
 * 分类下的商品简要信息（首页分类列表接口中的 goods 元素）
 */
data class CategoryGoodsItem(
    val id: String,
    val name: String,
    val desc: String,
    val price: String,
    val picture: String,
    val orderNum: Int
)
