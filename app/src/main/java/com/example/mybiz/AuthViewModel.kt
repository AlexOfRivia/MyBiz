package com.example.mybiz

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel()
{

    private val db = FirebaseDatabase.getInstance()

    var username by mutableStateOf("")

    private val auth = Firebase.auth

    var user by mutableStateOf(auth.currentUser)
        private set

    var isUserLoggedIn = mutableStateOf(auth.currentUser != null)
        private set

    init {
        auth.addAuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser

            if (user != null) {
                fetchUsername()
            } else {
                username = ""
            }
        }
    }

    fun signOut()
    {
        auth.signOut()
        isUserLoggedIn.value = false
    }

    fun deleteUser()
    {
        val userEmail = user?.email.toString()

        //deleting the username from db
        viewModelScope.launch {
            if(userEmail.isNotEmpty())
            {
                try {
                    val email = userEmail.replace(".","")

                    db.getReference("Users")
                        .child(email)
                        .removeValue()
                        .await()

                   user!!.delete().await()
                    isUserLoggedIn.value = false
                } catch (e: Exception) {
                    Log.e("Firebase delete", "Error while deleting")
                }

            }
        }

    }

    private fun fetchUsername()
    {
        val userEmail = user?.email.toString()

        viewModelScope.launch {
            if(userEmail.isNotEmpty())
            {
                try {
                    val email = userEmail.replace(".","")

                    val snapshot = db.getReference("Users")
                        .child(email)
                        .child("username")
                        .get()
                        .await()

                    username = snapshot.getValue(String::class.java) ?: ""

                } catch(e: Exception) {
                    Log.e("FirebaseError", "Error while fetching database", e)
                }
            }
        }
    }

    fun updateUsername(newUsername: String)
    {
        val userEmail = user?.email.toString()

        viewModelScope.launch {
            if(userEmail.isNotEmpty())
            {
                try {
                    val email = userEmail.replace(".","")

                    db.getReference("Users")
                        .child(email)
                        .child("username")
                        .setValue(newUsername)
                        .await()

                    username = newUsername
                } catch(e: Exception) {
                    Log.e("FirebaseError", "Error while fetching database", e)
                }
            }
        }
    }




}