package com.example.fall_mall_andorid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fall_mall_andorid.ui.screens.splash.SplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.fall_mall_andorid.navigation.FallMallNavGraph
import com.example.fall_mall_andorid.navigation.Screen
import com.example.fall_mall_andorid.ui.components.FallMallBottomBar
import com.example.fall_mall_andorid.ui.theme.Fall_mall_andoridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()//边缘效果
        setContent {
            Fall_mall_andoridTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onFinish = { showSplash = false })
                } else {
                    val navController = rememberNavController()//导航控制器
                    val backStackEntry = navController.currentBackStackEntryAsState()//当前导航条目
                    val currentRoute = backStackEntry.value?.destination?.route//当前路由

                    // 仅在主 Tab 页显示底部导航栏，子页面（如收藏）隐藏
                    val showBottomBar = currentRoute in setOf(
                        Screen.Home.route,
                        Screen.Category.route,
                        Screen.Cart.route,
                        Screen.Profile.route
                    )

                    //主页面架
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),//填充最大尺寸
                        //底部导航栏（子页面不显示）
                        bottomBar = {
                            if (showBottomBar) {
                                FallMallBottomBar(
                                    currentRoute = currentRoute,
                                    onItemClick = { screen ->
                                        if (currentRoute != screen.route) {
                                            navController.navigate(screen.route) {
                                                popUpTo(Screen.Home.route) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        FallMallNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }// End of Compose UI
    }
}
