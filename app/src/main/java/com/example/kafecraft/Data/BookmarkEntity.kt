package com.example.kafecraft.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val recipesId: String,
    val title: String,
    val description: String,
    val authorName: String
)
