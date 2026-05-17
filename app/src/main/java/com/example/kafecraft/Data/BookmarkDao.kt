package com.example.kafecraft.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kafecraft.data.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insert(bookmark: BookmarkEntity): Long

@Delete
suspend fun delete(bookmark: BookmarkEntity): Int

@Query("SELECT * FROM bookmarks")
fun getALLBookmarks(): Flow<List<BookmarkEntity>>


}