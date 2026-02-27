package com.example.fall_mall_andorid.data.model

/**
 * 登录接口返回的 result 用户信息
 * @see <a href="https://s.apifox.cn/b2a05857-d21a-4a59-b3f8-aaf35972c78d/api-180356229">登录-账号密码登录</a>
 */
data class LoginResult(
    val id: String,
    val account: String,
    val mobile: String,
    val token: String,
    val avatar: String,
    val nickname: String? = null,
    val gender: String? = null,
    val birthday: String? = null,
    val cityCode: String? = null,
    val provinceCode: String? = null,
    val profession: String? = null
)
