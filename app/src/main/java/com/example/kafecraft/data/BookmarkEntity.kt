package com.example.kafecraft.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val recipesId: String,
    val title: String,
    val description: String,
    val authorName: String
)
