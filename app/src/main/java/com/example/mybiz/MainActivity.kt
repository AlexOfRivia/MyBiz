package com.example.mybiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
                    var authViewModel: AuthViewModel = viewModel()
                    val navigationController = rememberNavController()
                    val startScreen = if(authViewModel.isUserLoggedIn.value) "dashboard_screen" else "main_screen"

                    NavHost(
                        navController = navigationController,
                        startDestination = startScreen
                    ){
                        //this is the route of all nav screens
                        composable("main_screen") {
                            MainScreen(navigationController) //passing the navigation controller for managing views
                        }
                        composable("register_screen") {
                            RegisterScreen(navigationController)
                        }
                        composable("login_screen") {
                            LoginScreen(navigationController)
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