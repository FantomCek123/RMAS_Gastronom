package com.example.gastronomad2.models.entities

import com.google.firebase.Timestamp

data class Comment(
    var commentId: String? = "",
    val restaurantId: String? = "",
    val userId: String? = "",
    val text: String? = "",
    val edited: Boolean = false,

    val username: String = ""
)
