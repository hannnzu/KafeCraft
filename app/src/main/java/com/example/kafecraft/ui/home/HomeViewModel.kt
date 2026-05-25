package com.example.kafecraft.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kafecraft.data.BookmarkDao
import com.example.kafecraft.data.BookmarkEntity
import com.example.kafecraft.data.Comment
import com.example.kafecraft.data.Recipe
import com.example.kafecraft.data.SessionManager
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RecipeWithID(val id: String, val recipe: Recipe)

class HomeViewModel(
    private val bookmarkDao: BookmarkDao,
    private val sessionManager: SessionManager
): ViewModel() {

    private val recipesRef = FirebaseDatabase.getInstance().getReference("recipes")
    private val commentsRef = FirebaseDatabase.getInstance().getReference("comments")

    var recipes by mutableStateOf<List<RecipeWithID>>(emptyList())
    var isLoading by mutableStateOf(true)
    var comments by mutableStateOf<List<Comment>>(emptyList())
    val bookmarks = bookmarkDao.getALLBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun fetchRecipes(){
        isLoading = true
        recipesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                recipes = snapshot.children.mapNotNull{ child ->
                    val recipe = child.getValue(Recipe::class.java)
                    val key = child.key
                    if (recipe != null && key != null) RecipeWithID(key, recipe)
                    else null
                }.reversed()
                isLoading = false
            }
            override fun onCancelled(error: DatabaseError) {
                isLoading = false
            }
        })
    }

    private fun fetchComment(recipeId: String){
        commentsRef.child(recipeId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                comments = snapshot.children.mapNotNull { it.getValue(Comment::class.java)}
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addComment(recipeId: String, text: String) {
        val comment = Comment(sessionManager.getUserName()?: "Unknown", text, System.currentTimeMillis())
        commentsRef.child(recipeId).push().setValue(comment)
    }

    fun toggleBookmark(recipeId: String,recipe: Recipe) {
        viewModelScope.launch {
            val entity = BookmarkEntity(recipeId,recipe.title,recipe.description, recipe.authorName)
            val isAlredyBookmarked = bookmarks.value.any { it.recipesId == recipeId}
            if (isAlredyBookmarked){
                bookmarkDao.delete(entity)
            } else {
                bookmarkDao.insert(entity)
            }
        }
    }
    fun removeBookmark(entity: BookmarkEntity) {
        viewModelScope.launch {
            bookmarkDao.delete(entity)
        }
    }

    class Factory(
        private val bookmarkDao: BookmarkDao,
        private val sessionManager: SessionManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = HomeViewModel(bookmarkDao, sessionManager) as T
    }
}