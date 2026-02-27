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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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

@Composable
fun ProfileScreen(navController: NavHostController) {
    val user by UserManager.currentUserFlow.collectAsState(initial = UserManager.getCurrentUser())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 顶部用户信息：已登录显示头像和昵称且不可点击跳转，未登录显示「点击登录」
        UserProfileHeader(user = user, navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        // 订单状态统计
        OrderStatusSection()

        Spacer(modifier = Modifier.height(16.dp))

        // 功能菜单列表
        FunctionMenuSection(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        // 设置选项（含退出登录）
        SettingsSection(onLogout = { UserManager.clearUser() })
    }
}

@Composable
private fun UserProfileHeader(user: LoginResult?, navController: NavHostController) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val clickModifier = if (user == null) modifier.clickable { navController.navigate(Screen.Login.route) } else modifier

    Card(
        modifier = clickModifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    text = if (user != null) user.mobile.ifBlank { "已登录" } else "点击登录/注册",
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "我的订单",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 订单状态行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待付款", count = 2)
                OrderStatusItem(icon = Icons.Default.ExitToApp, label = "待发货", count = 1)
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待收货", count = 0)
                OrderStatusItem(icon = Icons.Default.DateRange, label = "待评价", count = 3)
            }
        }
    }
}

@Composable
private fun OrderStatusItem(
    icon: ImageVector,
    label: String,
    count: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* TODO: 跳转到对应订单页面 */ }
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
private fun FunctionMenuSection(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            MenuItem(icon = Icons.Default.Favorite, label = "我的收藏") {
                navController.navigate(Screen.Favorites.route)
            }
            HorizontalDivider()
            MenuItem(icon = Icons.Default.LocationOn, label = "收货地址") { /* TODO */ }
            Divider()
            MenuItem(icon = Icons.Default.Notifications, label = "消息通知") { /* TODO */ }
            Divider()
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            MenuItem(icon = Icons.Default.Settings, label = "设置") { /* TODO */ }
            Divider()
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
