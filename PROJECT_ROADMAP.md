# Fall Mall 电商项目 - 开发路线图与 Kotlin 学习指南

## 项目简介
这是一个使用 **Kotlin + Jetpack Compose** 搭建的 Android 电商应用（Fall Mall）。在开发过程中会穿插 Kotlin 语法与最佳实践说明。

---

## 技术栈
| 技术 | 说明 |
|------|------|
| Kotlin | 主要开发语言 |
| Jetpack Compose | 声明式 UI |
| Navigation Compose | 页面导航 |
| Material 3 | 设计规范 |

---

## 开发阶段规划

### 第一阶段：基础架构（当前）
- [x] 修复构建配置
- [x] 添加导航依赖
- [x] 数据模型（商品、分类）
- [ ] 底部导航 + 首页框架

### 第二阶段：核心页面
- [ ] 首页：商品列表、分类入口
- [ ] 分类页：分类列表与筛选
- [ ] 购物车：增删改、数量
- [ ] 我的：用户信息、订单入口

### 第三阶段：详情与交互
- [ ] 商品详情页
- [ ] 加入购物车 / 立即购买
- [ ] 简单订单流程

### 第四阶段：优化与扩展
- [ ] 网络请求（如 Retrofit）+ 真实数据
- [ ] 本地持久化（Room 或 DataStore）
- [ ] 登录/注册（可选）

---

## Kotlin 学习要点（随代码推进）

1. **数据类 `data class`**：见 `model/Product.kt`、`Category.kt`
2. **可空类型 `?`、安全调用 `?.`、`!!`**
3. **集合与 lambda**：`listOf()`、`map`、`filter`
4. **Composable 与状态**：`@Composable`、`remember`、`mutableStateOf`
5. **密封类 `sealed class`**：用于导航或 UI 状态
6. **扩展函数、默认参数、命名参数**

每完成一个模块，会在对应文件里用注释标出「Kotlin 知识点」。
