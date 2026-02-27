package com.example.fall_mall_andorid.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fall_mall_andorid.data.api.AuthApi
import com.example.fall_mall_andorid.data.model.LoginResult
import com.example.fall_mall_andorid.data.network.HttpResult
import com.example.fall_mall_andorid.data.network.OkHttpManager
import com.example.fall_mall_andorid.data.session.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 登录页 UI 状态 */
sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val user: LoginResult) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(account: String, password: String) {
        if (account.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("请输入账号和密码")
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = AuthApi.login(account, password)) {
                is HttpResult.Success -> {
                    OkHttpManager.setAuthToken(result.data.token)
                    UserManager.setUser(result.data)
                    _uiState.value = LoginUiState.Success(result.data)
                }
                is HttpResult.Failure -> {
                    _uiState.value = LoginUiState.Error(result.message)
                }
            }
        }
    }

    fun clearError() {
        if (_uiState.value is LoginUiState.Error) _uiState.value = LoginUiState.Idle
    }

    /** 登录成功并已处理（如已返回上一页）后调用，重置状态避免再次进入仍为 Success */
    fun onSuccessHandled() {
        if (_uiState.value is LoginUiState.Success) _uiState.value = LoginUiState.Idle
    }
}
