package com.example.kafecraft.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kafecraft.data.SessionManager
import com.example.kafecraft.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(private val sessionManager: SessionManager) : ViewModel() {

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

    fun login(email: String, pass: String) {
        isLoading = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    isLoading = false
                    error = "Login gagal"
                    return@addOnSuccessListener
                }
                db.child(uid).get()
                    .addOnSuccessListener { snap ->
                        val user = snap.getValue(Users::class.java)
                        sessionManager.saveLoginSession(uid, user?.name ?: "", user?.email ?: email)
                        isLoading = false
                        isSuccess = true
                    }
                    .addOnFailureListener {
                        isLoading = false
                        error = it.message
                    }
            }
            .addOnFailureListener {
                isLoading = false
                error = it.message
            }
    }
    fun register(name: String, email: String, pass: String) {
        isLoading = true
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    isLoading = false
                    error = "Gagal mendapat ID"
                    return@addOnSuccessListener
                }
                db.child(uid).setValue(Users(name, email))
                    .addOnSuccessListener {
                        isLoading = false
                        isSuccess = true
                    }
                    .addOnFailureListener {
                        isLoading = false
                        error = it.message ?: "Gagal simpan data"
                    }
            }
            .addOnFailureListener {
                isLoading = false
                error = it.message
            }
    }

    fun sendPassworReset(email: String){
        isLoading = true
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                isLoading = false
                isSuccess = true
            }
            .addOnFailureListener {
                isLoading = false
                error = it.message
            }
    }
}
