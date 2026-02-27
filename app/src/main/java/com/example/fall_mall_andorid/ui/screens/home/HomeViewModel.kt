package com.example.fall_mall_andorid.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fall_mall_andorid.data.api.HomeApi
import com.example.fall_mall_andorid.data.model.home.BannerItem
import com.example.fall_mall_andorid.data.model.home.CategoryGoodsItem
import com.example.fall_mall_andorid.data.model.home.CategoryHeadItem
import com.example.fall_mall_andorid.data.network.HttpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val banners: List<BannerItem> = emptyList(),
    val categories: List<CategoryHeadItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            loadBanner()
            loadCategoryHead()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private suspend fun loadBanner() {
        when (val r = HomeApi.getBanner()) {
            is HttpResult.Success -> _uiState.value = _uiState.value.copy(banners = r.data)
            is HttpResult.Failure -> _uiState.value = _uiState.value.copy(error = r.message)
        }
    }

    private suspend fun loadCategoryHead() {
        when (val r = HomeApi.getCategoryHead()) {
            is HttpResult.Success -> _uiState.value = _uiState.value.copy(categories = r.data)
            is HttpResult.Failure -> if (_uiState.value.error == null) _uiState.value = _uiState.value.copy(error = r.message) else { /* keep banner error */ }
        }
    }

    /** 所有分类下的商品展平，用于特惠/爆款等推荐位 */
    fun getAllGoodsForRecommend(): List<CategoryGoodsItem> = _uiState.value.categories
        .flatMap { it.goods.orEmpty() }
        .distinctBy { it.id }
}
