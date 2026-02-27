package com.example.fall_mall_andorid.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request

/**
 * OkHttpManager 使用示例（仅作参考，不参与编译可删除）
 *
 * Kotlin 知识点：
 * - 协程：launch、suspend、withContext(Dispatchers.IO)
 * - when 穷举 sealed class，避免漏掉分支
 */
object ApiExample {

    fun exampleSuspend() {
        CoroutineScope(Dispatchers.Main).launch {
            // GET
            when (val result = OkHttpManager.get("https://api.example.com/data")) {
                is HttpResult.Success -> {
                    val json = result.data
                    // 解析 json 或直接使用
                }
                is HttpResult.Failure -> {
                    val msg = result.message
                    val code = result.code
                }
            }

            // GET 带参数
            val result2 = OkHttpManager.get(
                baseUrl = "https://api.example.com/list",
                params = mapOf("page" to "1", "size" to "10")
            )

            // POST 表单
            val result3 = OkHttpManager.postForm(
                url = "https://api.example.com/login",
                formParams = mapOf("username" to "user", "password" to "123")
            )

            // POST JSON
            val result4 = OkHttpManager.postJson(
                url = "https://api.example.com/order",
                jsonBody = """{"productId":"p1","quantity":2}"""
            )
        }
    }

    fun exampleCallback() {
        val request = Request.Builder()
            .url("https://api.example.com/data")
            .get()
            .build()
        OkHttpManager.enqueue(request) { result ->
            when (result) {
                is HttpResult.Success -> { /* result.data */ }
                is HttpResult.Failure -> { /* result.message */ }
            }
        }
    }
}
