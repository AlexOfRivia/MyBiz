package com.example.mybiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.*

class AuthViewModel : ViewModel()
{
    private val auth = Firebase.auth

    var isUserLoggedIn = mutableStateOf(auth.currentUser != null)
        private set

    fun signOut()
    {
        auth.signOut()
        isUserLoggedIn.value = false
    }
}