package com.example.fall_mall_andorid.data.model

/**
 * 商品数据模型
 *
 * Kotlin 知识点 ① data class
 * - 用 data class 声明“只存数据”的类，编译器会自动生成：
 *   equals()、hashCode()、toString()、copy()
 * - 适合做 JSON 映射、列表展示等
 */
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,  // Kotlin 知识点 ② 可空类型：? 表示可 null
    val imageUrl: String = "",           // Kotlin 知识点 ③ 默认参数：未传时用 ""
    val categoryId: String,
    val salesCount: Int = 0,
    val description: String = ""
) {
    /** 是否显示原价（有原价且原价大于现价时显示划线价） */
    val hasDiscount: Boolean
        get() = (originalPrice != null && originalPrice > price)

    /** 折扣描述，如 "8.5折" */
    val discountLabel: String?
        get() = if (hasDiscount && originalPrice != null && originalPrice > 0)
            "${(price / originalPrice * 10).toInt()}折"
        else null
}
