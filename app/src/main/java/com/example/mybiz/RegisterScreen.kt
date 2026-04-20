package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.*

@Composable
fun RegisterScreen(navController: NavController)
{
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current

    //initializing Firebase
    var auth = remember { Firebase.auth }
    auth = Firebase.auth
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Zarejestruj się",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 70.dp, bottom = 150.dp)
        )

        //username text field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Podaj nazwę użytkownika") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        //email text field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Podaj email") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        //password text field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Podaj hasło") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                if(password.isNotEmpty() && email.isNotEmpty())
                {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            navController.navigate("dashboard_screen")
                        } else {
                            android.widget.Toast.makeText(context, "Error: ${task.exception?.message}",android.widget.Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier.width(200.dp).padding(bottom=50.dp, top = 50.dp)
        ) {
            Text("Dalej")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview()
{
    MyBizTheme(darkTheme = true) {
        RegisterScreen(navController = rememberNavController())
    }
}