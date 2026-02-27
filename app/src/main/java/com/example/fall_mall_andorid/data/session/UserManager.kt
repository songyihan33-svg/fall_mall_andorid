package com.example.fall_mall_andorid.data.session

import com.example.fall_mall_andorid.data.model.login.LoginResult
import com.example.fall_mall_andorid.data.network.OkHttpManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 当前登录用户状态（内存持有，进程内有效）
 * 登录成功调用 setUser，退出登录调用 clearUser
 */
object UserManager {

    private val _currentUser = MutableStateFlow<LoginResult?>(null)
    val currentUserFlow: StateFlow<LoginResult?> = _currentUser.asStateFlow()

    fun setUser(user: LoginResult) {
        _currentUser.value = user
    }

    /** 退出登录：清空用户并清除网络层 Token */
    fun clearUser() {
        _currentUser.value = null
        OkHttpManager.clearAuthToken()
    }

    fun getCurrentUser(): LoginResult? = _currentUser.value
}
