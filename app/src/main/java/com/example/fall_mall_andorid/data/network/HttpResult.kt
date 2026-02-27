package com.example.fall_mall_andorid.data.network

/**
 * 网络请求结果封装
 *
 * Kotlin 知识点：密封类 sealed class
 * - 表示“有限几种结果”之一，便于 when 穷举、避免漏写分支
 * - Success 带数据，Failure 带异常或错误信息
 */
sealed class HttpResult<out T> {
    data class Success<T>(val data: T) : HttpResult<T>()
    data class Failure(val message: String, val code: Int = -1, val throwable: Throwable? = null) : HttpResult<Nothing>()
}
