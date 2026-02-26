package com.example.fall_mall_andorid.data.model

/**
 * 商品分类模型
 *
 * Kotlin 知识点：data class 可以只有属性，不需要 body；
 * 这里加了一个简单属性 icon，用于 UI 展示（实际项目可从网络或资源加载）。
 */
data class Category(
    val id: String,
    val name: String,
    val icon: String = "",  // 可以是 emoji 或图标资源名
    val sortOrder: Int = 0
)
