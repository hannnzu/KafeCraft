package com.example.kafecraft.ui.myrecipes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kafecraft.data.Recipe
import com.example.kafecraft.data.SessionManager
import com.example.kafecraft.ui.home.RecipeWithID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyRecipesViewModel(private val sessiaonManager : SessionManager) : ViewModel(){
    private val recipeRef = FirebaseDatabase.getInstance().getReference("recipes")
    private  var recipeListener: ValueEventListener? = null
    private var lastQueryUid: String? = null

    var myRecipes by mutableStateOf<List<RecipeWithID>>(emptyList())
    var isLoading by mutableStateOf(true)
    var isSaving by mutableStateOf(false)
    var saveMessage by mutableStateOf<String?>(null)

    init { fetchMyRecipes() }

    fun fetchMyRecipes(){
        val uid = sessiaonManager.getUserId()
        if (uid == null) {
             isLoading = false
            return
        }
        if (uid == lastQueryUid && recipeListener != null) return

        recipeListener?.let { recipeRef.removeEventListener(it)}
        lastQueryUid = uid

        isLoading = true
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myRecipes = snapshot.children.mapNotNull { child ->
                    val recipe = child.getValue(Recipe::class.java)
                    val key = child.key
                    if ( recipe!=null && key != null) RecipeWithID(key, recipe) else null
                }.reversed()
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading = false
            }
        }
        recipeListener = listener
        recipeRef.orderByChild("authorId").equalTo(uid).addValueEventListener(listener)
    }
    override fun onCleared() {
        super.onCleared()
        recipeListener?.let{ recipeRef.removeEventListener(it)}
    }

    fun saveRecipe(
        title: String,
        description: String,
        existingRecipeId: String? = null
    ) {
        val uid = sessiaonManager.getUserId()
        val userName = sessiaonManager.getUserName()
        if (uid == null || userName == null) {
            saveMessage = "User tidak sitemukan, harap login ulang."
            return
        }
        isSaving = true
        saveToDb(uid, userName,title, description, existingRecipeId)
    }

    private  fun saveToDb(
        authorId: String,
        authorName: String,
        title: String,
        description: String,
        existingId: String?
    ) {
        val recipe = Recipe(authorId, authorName, title, description, System.currentTimeMillis())
        val ref = if (existingId != null) recipeRef.child(existingId) else recipeRef.push()
        ref.setValue(recipe)
            .addOnSuccessListener {
                isSaving = false
                saveMessage = " Resep berhasil disimpan."
            }
            .addOnFailureListener {
                isSaving = false
                saveMessage = " gagal menyiman resep : ${it.message}"
            }


    }

    fun deleteRecipe(recipeId: String) {
        isSaving = true
        recipeRef.child(recipeId).removeValue()
            .addOnSuccessListener {
                isSaving = false
                saveMessage = "Resep berhasil disimpan"
            }
            .addOnFailureListener {
                isSaving = false
                saveMessage = "Gagal menhapus resep"
            }
    }

    fun resetMessage() { saveMessage = null }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED-CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = MyRecipesViewModel(sessionManager) as T
        }
}