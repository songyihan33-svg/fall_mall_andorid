package com.example.fall_mall_andorid.data.api

import com.example.fall_mall_andorid.data.model.home.BannerItem
import com.example.fall_mall_andorid.data.model.home.CategoryHeadItem
import com.example.fall_mall_andorid.data.model.home.HotPreferenceResult
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

    /**
     * 首页-分类列表
     * GET /home/category/head
     * 若已设置 Token，请求会统一携带 Authorization 头
     */
    suspend fun getCategoryHead(): HttpResult<List<CategoryHeadItem>> {
        val url = "${ApiConstants.BASE_URL}/home/category/head"
        return when (val raw = OkHttpManager.get(url)) {
            is HttpResult.Success -> parseCategoryHeadResponse(raw.data)
            is HttpResult.Failure -> HttpResult.Failure(raw.message, raw.code, raw.throwable)
        }
    }

    /**
     * 首页-特惠推荐
     * GET /hot/preference
     * 返回 result：含 title、subTypes（子类型及各自 goodsItems 分页商品）
     */
    suspend fun getHotPreference(): HttpResult<HotPreferenceResult> {
        val url = "${ApiConstants.BASE_URL}/hot/preference"
        return when (val raw = OkHttpManager.get(url)) {
            is HttpResult.Success -> parseHotPreferenceResponse(raw.data)
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

    private fun parseCategoryHeadResponse(json: String): HttpResult<List<CategoryHeadItem>> {
        return try {
            val type = object : TypeToken<ApiResponse<List<CategoryHeadItem>>>() {}.type
            val response: ApiResponse<List<CategoryHeadItem>> = gson.fromJson(json, type)
            if (response.result != null) {
                HttpResult.Success(response.result)
            } else {
                HttpResult.Failure(response.msg)
            }
        } catch (e: Exception) {
            HttpResult.Failure(e.message ?: "Parse error", throwable = e)
        }
    }

    private fun parseHotPreferenceResponse(json: String): HttpResult<HotPreferenceResult> {
        return try {
            val type = object : TypeToken<ApiResponse<HotPreferenceResult>>() {}.type
            val response: ApiResponse<HotPreferenceResult> = gson.fromJson(json, type)
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
