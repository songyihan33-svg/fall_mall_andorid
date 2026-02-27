package com.example.fall_mall_andorid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fall_mall_andorid.ui.screens.CartScreen
import com.example.fall_mall_andorid.ui.screens.CategoryScreen
import com.example.fall_mall_andorid.ui.screens.FavoritesScreen
import com.example.fall_mall_andorid.ui.screens.HomeScreen
import com.example.fall_mall_andorid.ui.screens.ProfileScreen
import com.example.fall_mall_andorid.ui.screens.login.LoginScreen

/**
 * Kotlin 知识点：枚举类 enum class
 * 用枚举表示路由，避免硬编码字符串，便于维护和跳转。
 */
enum class Screen(val route: String) {
    Home("home"),
    Category("category"),
    Cart("cart"),
    Profile("profile"),
    // 新增的个人中心相关页面
    Favorites("favorites"),
    Login("login"),
    Address("address"),
    Notifications("notifications"),
    Help("help"),
    Settings("settings")
}

//标记这是一个「可组合函数」，能在 Compose 界面中调用，用于构建 UI / 逻辑
@Composable
fun FallMallNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Category.route) { CategoryScreen() }
        composable(Screen.Cart.route) { CartScreen() }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        // 新增的页面路由
        composable(Screen.Favorites.route) { FavoritesScreen(navController = navController) }
        composable(Screen.Login.route) { LoginScreen(navController = navController) }
    }
}
