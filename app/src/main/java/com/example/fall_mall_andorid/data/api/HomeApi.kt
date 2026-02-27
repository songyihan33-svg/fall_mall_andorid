package com.example.fall_mall_andorid.data.api

import com.example.fall_mall_andorid.data.model.BannerItem
import com.example.fall_mall_andorid.data.network.ApiResponse
import com.example.fall_mall_andorid.data.network.HttpResult
import com.example.fall_mall_andorid.data.network.OkHttpManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 首页相关接口
 */
object HomeApi {

    private val gson = Gson()

    /**
     * 首页 Banner
     * GET /home/banner
     * 若已调用 OkHttpManager.setAuthToken(token)，请求会统一携带 Authorization 头
     */
    suspend fun getBanner(): HttpResult<List<BannerItem>> {
        val url = "${ApiConstants.BASE_URL}/home/banner"
        return when (val raw = OkHttpManager.get(url)) {
            is HttpResult.Success -> parseBannerResponse(raw.data)
            is HttpResult.Failure -> HttpResult.Failure(raw.message, raw.code, raw.throwable)
        }
    }

    private fun parseBannerResponse(json: String): HttpResult<List<BannerItem>> {
        return try {
            val type = object : TypeToken<ApiResponse<List<BannerItem>>>() {}.type
            val response: ApiResponse<List<BannerItem>> = gson.fromJson(json, type)
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
