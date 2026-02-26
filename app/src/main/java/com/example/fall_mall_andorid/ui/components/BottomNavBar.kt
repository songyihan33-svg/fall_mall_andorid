package com.example.fall_mall_andorid.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.fall_mall_andorid.navigation.Screen

/**
 * 底部导航项数据
 * Kotlin 知识点：data class 用于简单结构，这里用密封类也可以。
 */
data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun FallMallBottomBar(
    currentRoute: String?,
    onItemClick: (Screen) -> Unit
) {
    val items = listOf(
        BottomNavItem(Screen.Home, "首页", Icons.Filled.Home, Icons.Filled.Home),
        BottomNavItem(Screen.Category, "分类", Icons.Filled.List, Icons.Filled.List),
        BottomNavItem(Screen.Cart, "购物车", Icons.Filled.ShoppingCart, Icons.Filled.ShoppingCart),
        BottomNavItem(Screen.Profile, "我的", Icons.Filled.Person, Icons.Filled.Person)
    )

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
