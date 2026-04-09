package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun PasswordResetScreen(navController: NavController)
{
    var email by remember { mutableStateOf("") }

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
            text = "Resetuj hasło",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 70.dp, bottom = 200.dp)
        )

        OutlinedTextField (
            value = email,
            onValueChange = { email = it },
            label = { Text("Podaj email ") },
            modifier = Modifier.padding(bottom = 50.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                if(email.isNotEmpty())
                {
                    auth?.sendPasswordResetEmail(email)?.addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            android.widget.Toast.makeText(context, "Email resetu hasła został wysłany.", android.widget.Toast.LENGTH_LONG).show()
                        } else {
                            android.widget.Toast.makeText(context, "Error: ${task.exception?.message}", android.widget.Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    android.widget.Toast.makeText(context, "Pole nie może być puste.", android.widget.Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.width(200.dp).padding(bottom = 50.dp, top = 80.dp)
        ) {
            Text("Dalej")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordResetScreenPreview()
{
    MyBizTheme(darkTheme = true) {
        PasswordResetScreen(navController = rememberNavController())
    }
}