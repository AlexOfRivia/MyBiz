package com.example.mybiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp.*
import com.google.firebase.auth.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBizTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()

                    NavHost(
                        navController = navigationController,
                        startDestination = "main_screen"
                    ) {
                        //this it the route of all nav screens
                        composable("main_screen") {
                            MainScreen(navigationController) //passing the navigation controller for managing views
                        }
                        composable("register_screen") {
                            RegisterScreen(navigationController) //passing the navigation controller for managing views
                        }
                        composable("login_screen") {
                            LoginScreen(navigationController) //passing the navigation controller for managing views
                        }
                        composable("dashboard_screen") {
                            DashboardScreen(navigationController)
                        }
                        composable("password_reset_screen") {
                            PasswordResetScreen(navigationController)
                        }
                    }
                }
            }
        }
    }
}