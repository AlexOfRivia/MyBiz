package com.example.mybiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.*

class AuthViewModel : ViewModel()
{
    private val auth = Firebase.auth

    var user by mutableStateOf(auth.currentUser)
        private set

    var isUserLoggedIn = mutableStateOf(auth.currentUser != null)
        private set

    init {
        auth.addAuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
        }
    }

    fun signOut()
    {
        auth.signOut()
        isUserLoggedIn.value = false
    }

}