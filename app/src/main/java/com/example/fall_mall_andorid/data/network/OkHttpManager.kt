package com.example.fall_mall_andorid.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * OkHttp 封装类：统一管理 Client、超时、拦截器，提供同步/协程请求方法
 *
 * Kotlin 知识点：
 * - object：单例，整个应用共用一个 OkHttpClient（推荐）
 * - 扩展属性、默认参数
 */
object OkHttpManager {

    /** 默认连接 / 读写超时 */
    private const val CONNECT_TIMEOUT = 15L
    private const val READ_TIMEOUT = 20L
    private const val WRITE_TIMEOUT = 20L

    /** 默认 JSON 请求体类型 */
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    /** 统一认证 Token，登录后设置、退出时清空。拦截器会据此自动添加 Authorization 头 */
    @Volatile
    private var authToken: String? = null

    /**
     * 设置认证 Token，之后所有请求会自动携带 Header：Authorization: Bearer {token}
     * 登录成功后调用，如：OkHttpManager.setAuthToken(token)
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }

    /**
     * 清空 Token（退出登录时调用）
     */
    fun clearAuthToken() {
        authToken = null
    }

    /**
     * 单例 OkHttpClient
     * - 认证拦截器：有 token 时自动加 Authorization
     * - 日志拦截器：打印请求/响应
     * 注意：属性名用 okHttpClient 避免与下方 getClient() 方法 JVM 签名冲突（属性会生成 getClient() getter）
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY) // 直连接口，不走系统代理，避免代理 192.168.1.34:7890 不可达导致登录失败
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private fun createAuthInterceptor(): Interceptor = Interceptor { chain ->
        val token = authToken
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(request)
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * 获取 OkHttpClient 实例（供需要自定义请求时使用）
     */
    fun getClient(): OkHttpClient = okHttpClient

    /**
     * GET 请求（协程挂起，在 IO 线程执行）
     * @param url 完整 URL
     * @param headers 可选请求头，如 mapOf("Authorization" to "Bearer xxx")
     */
    suspend fun get(
        url: String,
        headers: Map<String, String> = emptyMap()
    ): HttpResult<String> = withContext(Dispatchers.IO) {
        executeRequest(
            request = Request.Builder()
                .url(url)
                .apply { headers.forEach { (k, v) -> addHeader(k, v) } }
                .get()
                .build()
        )
    }

    /**
     * GET 请求，带 Query 参数
     * @param baseUrl 如 "https://api.example.com/list"
     * @param params 如 mapOf("page" to "1", "size" to "10")
     */
    suspend fun get(
        baseUrl: String,
        params: Map<String, String>,
        headers: Map<String, String> = emptyMap()
    ): HttpResult<String> = withContext(Dispatchers.IO) {
        val urlBuilder = baseUrl.toHttpUrlOrNull()?.newBuilder() ?: run {
            return@withContext HttpResult.Failure("Invalid URL: $baseUrl")
        }
        params.forEach { (k, v) -> urlBuilder.addQueryParameter(k, v) }
        executeRequest(
            request = Request.Builder()
                .url(urlBuilder.build())
                .apply { headers.forEach { (k, v) -> addHeader(k, v) } }
                .get()
                .build()
        )
    }

    /**
     * POST 表单（application/x-www-form-urlencoded）
     */
    suspend fun postForm(
        url: String,
        formParams: Map<String, String>,
        headers: Map<String, String> = emptyMap()
    ): HttpResult<String> = withContext(Dispatchers.IO) {
        val formBody = FormBody.Builder().apply {
            formParams.forEach { (k, v) -> add(k, v) }
        }.build()
        executeRequest(
            request = Request.Builder()
                .url(url)
                .apply { headers.forEach { (k, v) -> addHeader(k, v) } }
                .post(formBody)
                .build()
        )
    }

    /**
     * POST JSON 请求体
     */
    suspend fun postJson(
        url: String,
        jsonBody: String,
        headers: Map<String, String> = emptyMap()
    ): HttpResult<String> = withContext(Dispatchers.IO) {
        val body = jsonBody.toRequestBody(JSON_MEDIA_TYPE)
        executeRequest(
            request = Request.Builder()
                .url(url)
                .apply { headers.forEach { (k, v) -> addHeader(k, v) } }
                .post(body)
                .build()
        )
    }

    /**
     * 同步执行 Request，将结果封装为 HttpResult<String>
     */
    private fun executeRequest(request: Request): HttpResult<String> {
        return try {
            okHttpClient.newCall(request).execute().use { response ->
                val body = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    HttpResult.Success(body)
                } else {
                    HttpResult.Failure(
                        message = body.ifEmpty { "HTTP ${response.code}" },
                        code = response.code
                    )
                }
            }
        } catch (e: Exception) {
            HttpResult.Failure(
                message = e.message ?: "Unknown error",
                throwable = e
            )
        }
    }

    /**
     * 回调方式请求（不挂起协程时可用）
     * Kotlin 知识点：高阶函数，lambda 作为参数
     */
    fun enqueue(
        request: Request,
        onResult: (HttpResult<String>) -> Unit
    ) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(HttpResult.Failure(e.message ?: "Network error", throwable = e))
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                response.use {
                    val body = it.body?.string() ?: ""
                    onResult(
                        if (it.isSuccessful) HttpResult.Success(body)
                        else HttpResult.Failure(body.ifEmpty { "HTTP ${it.code}" }, code = it.code)
                    )
                }
            }
        })
    }
}
