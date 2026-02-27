package com.example.fall_mall_andorid.data.model.home.category

/**
 * 首页-分类列表 单项（一级分类）
 * 对应接口 GET /home/category/head 的 result 数组元素
 * @see <a href="https://s.apifox.cn/b2a05857-d21a-4a59-b3f8-aaf35972c78d/api-180356225">首页-分类列表</a>
 */
data class CategoryHeadItem(
    val id: String,
    val name: String,
    val picture: String,
    val children: List<CategoryHeadChild>,
    val goods: List<CategoryGoodsItem>
)