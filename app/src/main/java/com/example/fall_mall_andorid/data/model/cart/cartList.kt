package com.example.fall_mall_andorid.data.model.cart

data class cartList (
    val id: String,
    val skuId: String,
    val name: String,
    val attrsText: String,
    val specs: List<String>,
    val picture: String,
    val price: String,
    val nowPrice: String,
    val nowOriginalPrice: String,
    val selected: Boolean,
    val stock: Int,
    val count: Int,
    val isEffective: Boolean,
    val discount: Double?,
    val isCollect: Boolean,
    val postFee: Int
)