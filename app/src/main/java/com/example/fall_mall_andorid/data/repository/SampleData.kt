package com.example.fall_mall_andorid.data.repository

import com.example.fall_mall_andorid.data.model.Category
import com.example.fall_mall_andorid.data.model.Product

/**
 * 模拟数据源，后续可替换为网络或数据库
 *
 * Kotlin 知识点：
 * - listOf() 创建只读列表
 * - 最后一行的表达式即函数返回值，无需 return
 */
object SampleData {

    val categories: List<Category> = listOf(
        Category(id = "1", name = "数码", icon = "📱", sortOrder = 1),
        Category(id = "2", name = "服饰", icon = "👕", sortOrder = 2),
        Category(id = "3", name = "食品", icon = "🍜", sortOrder = 3),
        Category(id = "4", name = "家居", icon = "🏠", sortOrder = 4),
        Category(id = "5", name = "美妆", icon = "💄", sortOrder = 5)
    )

    val products: List<Product> = listOf(
        Product(
            id = "p1",
            name = "无线蓝牙耳机",
            price = 199.0,
            originalPrice = 299.0,
            categoryId = "1",
            salesCount = 1200,
            description = "降噪长续航"
        ),
        Product(
            id = "p2",
            name = "智能手表",
            price = 899.0,
            originalPrice = 999.0,
            categoryId = "1",
            salesCount = 560
        ),
        Product(
            id = "p3",
            name = "纯棉 T 恤",
            price = 79.0,
            categoryId = "2",
            salesCount = 3200
        ),
        Product(
            id = "p4",
            name = "有机坚果礼盒",
            price = 128.0,
            originalPrice = 168.0,
            categoryId = "3",
            salesCount = 890
        ),
        Product(
            id = "p5",
            name = "香薰加湿器",
            price = 159.0,
            categoryId = "4",
            salesCount = 450
        )
    )

    /** 根据分类 id 获取商品列表（Kotlin：filter 过滤集合） */
    fun getProductsByCategory(categoryId: String): List<Product> =
        products.filter { it.categoryId == categoryId }
}
