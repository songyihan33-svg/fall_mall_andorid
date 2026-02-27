package com.example.fall_mall_andorid.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fall_mall_andorid.data.model.home.banner.BannerItem
import com.example.fall_mall_andorid.data.model.home.category.CategoryGoodsItem
import com.example.fall_mall_andorid.data.model.home.category.CategoryHeadItem
import com.example.fall_mall_andorid.ui.screens.home.HomeViewModel

private val SearchBarBeige = Color(0xFFF5F0E8)//背景
private val PromoRed = Color(0xFFC41E3A)
private val PromoGold = Color(0xFFD4A84B)
private val TagOrange = Color(0xFFE85D04)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            HomeTopBar()
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            BannerSection(banners = state.banners)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CategorySection(categories = state.categories)
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            SpecialOfferSection(
                promoTitle = "年终大促 限时买就返",
                promoSubtitle = "~活动即将开始~",
                goods = viewModel.getAllGoodsForRecommend()
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            SectionTitle(left = "爆款推荐 最受欢迎", right = null)
            Spacer(modifier = Modifier.height(8.dp))
            GoodsHorizontalRow(goods = viewModel.getAllGoodsForRecommend().take(6))
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionTitle(left = "一站买全 精心优选", right = null)
            Spacer(modifier = Modifier.height(8.dp))
            GoodsHorizontalRow(goods = viewModel.getAllGoodsForRecommend().drop(6).take(6))
        }
    }

    if (state.isLoading && state.banners.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(
                    text = "Q 搜索...",
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SearchBarBeige,
                unfocusedContainerColor = SearchBarBeige,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier.widthIn(min = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Fall Mall",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerSection(banners: List<BannerItem>) {
    if (banners.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SearchBarBeige),
            contentAlignment = Alignment.Center
        ) {
            Text("暖冬上新季", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        return
    }
    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp),
            pageSpacing = 12.dp,
            userScrollEnabled = true
        ) { page ->
            val item = banners[page]
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { /* 可后续用 item.hrefUrl 跳转 */ },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AsyncImage(
                    model = item.imgUrl,
                    contentDescription = item.type.ifBlank { "Banner" },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            banners.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.LightGray
                        )
                )
            }
        }
    }
}

@Composable
private fun CategorySection(categories: List<CategoryHeadItem>) {
    if (categories.isEmpty()) return
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(72.dp)
                    .clickable { }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (category.picture.isNotBlank()) {
                        AsyncImage(
                            model = category.picture,
                            contentDescription = category.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("📦", style = MaterialTheme.typography.titleLarge)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun SectionTitle(left: String, right: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = left,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (right != null) {
            Text(text = right, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
private fun SpecialOfferSection(
    promoTitle: String,
    promoSubtitle: String,
    goods: List<CategoryGoodsItem>
) {
    SectionTitle(left = "特惠推荐", right = "精选全攻略")
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier
                .width(120.dp)
                .height(200.dp)
                .clickable { },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PromoRed),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = promoTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = promoSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = PromoGold
                )
            }
        }
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(goods.take(4)) { item ->
                SmallGoodsCard(item = item)
            }
        }
    }
}

@Composable
private fun SmallGoodsCard(item: CategoryGoodsItem) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (item.picture.isNotBlank()) {
                    AsyncImage(
                        model = item.picture,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("📦", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleLarge)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¥${item.price}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PromoRed
                )
            }
        }
    }
}

@Composable
private fun GoodsHorizontalRow(goods: List<CategoryGoodsItem>) {
    if (goods.isEmpty()) return
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(goods) { item ->
            MediumGoodsCard(item = item)
        }
    }
}

@Composable
private fun MediumGoodsCard(item: CategoryGoodsItem) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable { },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (item.picture.isNotBlank()) {
                    AsyncImage(
                        model = item.picture,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("📦", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.headlineMedium)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¥${item.price}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PromoRed
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PromoRed.copy(alpha = 0.15f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text("立减 90", fontSize = 10.sp, color = PromoRed)
                }
            }
        }
    }
}
