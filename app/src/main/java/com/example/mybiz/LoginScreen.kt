package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.*

@Composable
fun LoginScreen(navController: NavController)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current

    //initializing Firebase
    var auth = if(androidx.compose.ui.platform.LocalInspectionMode.current) {
        null
    } else {
        Firebase.auth
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Zaloguj się",
            color = Color(47, 186, 63),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 70.dp, bottom = 200.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Podaj email") },
            modifier = Modifier.padding(bottom = 50.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Podaj hasło") },
            modifier = Modifier.padding(bottom = 20.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        //gotta add forgot password here
        TextButton(
            onClick = { navController.navigate("password_reset_screen") },
                modifier = Modifier.padding(bottom = 60.dp),

            ) {
                    Text(
                        text = "Resetuj hasło",
                        color = Color(47, 186, 63)
                    )
            }

        ElevatedButton(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("dashboard_screen")
                            } else {
                                android.widget.Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 50.dp, top = 60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
                ) {
                    Text(
                        text = "Dalej",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun LoginScreenPreview()
    {
        MyBizTheme(darkTheme = true) {
            LoginScreen(navController = rememberNavController())
        }
    }