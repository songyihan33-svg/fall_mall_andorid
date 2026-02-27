package com.example.fall_mall_andorid.data.network

import com.google.gson.annotations.SerializedName

/**
 * 美科接口统一响应结构：{ code, msg, result }
 * Kotlin 知识点：泛型 T，result 可为任意类型（List、对象等）
 */
data class ApiResponse<T>(
    val code: String,
    val msg: String,
    val result: T?
)
