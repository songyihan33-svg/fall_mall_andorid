package com.example.fall_mall_andorid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.fall_mall_andorid.data.model.LoginResult
import com.example.fall_mall_andorid.data.session.UserManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fall_mall_andorid.navigation.Screen

private val ProfileHeaderBg = Color(0xFFFEFEFE) // 我的页顶部背景

@Composable
fun ProfileScreen(navController: NavHostController) {
    val user by UserManager.currentUserFlow.collectAsState(initial = UserManager.getCurrentUser())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        // 顶部背景条 + 用户卡片
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                UserProfileHeader(user = user, navController = navController)
            }
        }

        //功能菜单
        Spacer(modifier = Modifier.height(12.dp))
        FunctionMenuSection1(navController = navController)

        // 我的订单
        Spacer(modifier = Modifier.height(12.dp))
        OrderStatusSection()

        // 功能菜单
        Spacer(modifier = Modifier.height(12.dp))
        FunctionMenuSection2(navController = navController)

        // 设置与退出
        Spacer(modifier = Modifier.height(12.dp))
        SettingsSection(onLogout = { UserManager.clearUser() })
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun UserProfileHeader(user: LoginResult?, navController: NavHostController) {
    val modifier = Modifier.fillMaxWidth()
    val clickModifier = if (user == null) modifier.clickable { navController.navigate(Screen.Login.route) } else modifier

    Card(
        modifier = clickModifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 用户头像：已登录用网络头像，未登录用占位图标
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                if (user != null && user.avatar.isNotBlank()) {
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = "用户头像",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "用户头像",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 用户信息：已登录显示昵称/账号，未登录显示「点击登录/注册」
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (user != null) (user.nickname?.takeIf { it.isNotBlank() } ?: user.account) else "用户名",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user?.mobile?.ifBlank { "已登录" } ?: "点击登录/注册",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 编辑按钮（已登录可点，未登录不跳转登录）
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "编辑信息",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* TODO: 跳转到编辑页面 */ }
            )
        }
    }
}

@Composable
private fun OrderStatusSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "我的订单",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "全部订单",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { /* TODO: 订单列表 */ }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OrderStatusItem(icon = Icons.Default.DateRange, label = "全部订单", count = 2, modifier = Modifier.weight(1f))
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待付款", count = 1, modifier = Modifier.weight(1f))
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待发货", count = 0, modifier = Modifier.weight(1f))
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待收货", count = 3, modifier = Modifier.weight(1f))
                OrderStatusItem(icon = Icons.Default.MailOutline, label = "待评价", count = 0, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun OrderStatusItem(
    icon: ImageVector,
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            if (count > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (count > 99) "99+" else count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun FunctionMenuSection1(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "我的订单",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Text(
//                    text = "全部订单",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    modifier = Modifier.clickable { /* TODO: 订单列表 */ }
//                )
//            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OrderStatusItem(icon = Icons.Default.Favorite, label = "我的收藏", count = 2, modifier = Modifier.weight(1f), onClick = {
                    navController.navigate(Screen.Favorites.route)
                })
                OrderStatusItem(icon = Icons.Default.CheckCircle, label = "我的足迹", count = 1, modifier = Modifier.weight(1f))
                OrderStatusItem(icon = Icons.Default.Person, label = "我的客服", count = 0, modifier = Modifier.weight(1f))
            }
        }
    }
}
@Composable
private fun FunctionMenuSection2(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuItem(icon = Icons.Default.Favorite, label = "我的收藏") {
                navController.navigate(Screen.Favorites.route)
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            MenuItem(icon = Icons.Default.LocationOn, label = "收货地址") { /* TODO */ }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            MenuItem(icon = Icons.Default.Notifications, label = "消息通知") { /* TODO */ }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            MenuItem(icon = Icons.Default.Person, label = "帮助与客服") { /* TODO */ }
        }
    }
}

@Composable
private fun SettingsSection(onLogout: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuItem(icon = Icons.Default.Settings, label = "设置") { /* TODO */ }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            MenuItem(icon = Icons.Default.ExitToApp, label = "退出登录", textColor = Color.Red, onClick = onLogout)
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    label: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "前往",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}
