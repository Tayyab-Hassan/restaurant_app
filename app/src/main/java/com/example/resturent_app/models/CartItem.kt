package com.example.resturent_app.models

import java.io.Serializable

data class CartItem(
    val product: Product,
    var quantity: Int
) : Serializable