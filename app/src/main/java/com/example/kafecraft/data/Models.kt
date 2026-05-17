package com.example.kafecraft.data

import com.google.firebase.Timestamp

data class Users(
    val name: String = "",
    val email: String = ""
)

data class Recipe(
    val authorId: String = "",
    val authorName: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = 0L
)

data class Comment(
    val authorName: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
