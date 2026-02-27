package com.example.fall_mall_andorid.data.api

import com.example.fall_mall_andorid.data.model.login.LoginResult
import com.example.fall_mall_andorid.data.network.ApiResponse
import com.example.fall_mall_andorid.data.network.HttpResult
import com.example.fall_mall_andorid.data.network.OkHttpManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 登录等认证接口
 * POST /login 账号密码登录
 */
object AuthApi {

    private val gson = Gson()

    /**
     * 账号密码登录
     * POST /login，Body: { "account", "password" }
     * 成功返回用户信息（含 token），调用方需自行调用 OkHttpManager.setAuthToken(result.token)
     */
    suspend fun login(account: String, password: String): HttpResult<LoginResult> {
        val url = "${ApiConstants.BASE_URL}/login"
        val body = gson.toJson(mapOf("account" to account, "password" to password))
        return when (val raw = OkHttpManager.postJson(url, jsonBody = body)) {
            is HttpResult.Success -> parseLoginResponse(raw.data)
            is HttpResult.Failure -> HttpResult.Failure(raw.message, raw.code, raw.throwable)
        }
    }

    private fun parseLoginResponse(json: String): HttpResult<LoginResult> {
        return try {
            val type = object : TypeToken<ApiResponse<LoginResult>>() {}.type
            val response: ApiResponse<LoginResult> = gson.fromJson(json, type)
            if (response.result != null) {
                HttpResult.Success(response.result)
            } else {
                HttpResult.Failure(response.msg)
            }
        } catch (e: Exception) {
            HttpResult.Failure(e.message ?: "Parse error", throwable = e)
        }
    }
}
