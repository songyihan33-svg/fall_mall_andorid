package com.example.fall_mall_andorid.data.model.home

/**
 * 首页分类下的二级分类
 */
data class CategoryHeadChild(
    val id: String,
    val name: String,
    val picture: String,
    val children: List<CategoryHeadChild>?=null,
    val goods: List<CategoryGoodsItem>?=null
)
