package com.example.kafecraft.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "kafecraft_session"
        private const val KEY_IS_LOGGED_IN = "user_id"
        private const val KEY_USER_ID = "user_email"
    }


}