package com.example.kafecraft.ui.auth

import android.media.session.MediaSessionManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(private val sessionManager: MediaSessionManager) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun resetState(){
        isLoading = false
        error = null
        isSuccess = false
    }

    fun Login(email:String, password:String){

    }
}