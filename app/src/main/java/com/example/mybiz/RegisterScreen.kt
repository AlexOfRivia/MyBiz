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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme

@Composable
fun RegisterScreen(navController: NavController)
{
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
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

        OutlinedTextField(
            value = username,
            onValueChange = {

            },
            label = { Text("Podaj nazwę użytkownika") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {

            },
            label = { Text("Podaj email") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {

            },
            label = { Text("Podaj hasło") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                /*przejście do ekranu głównego*/
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
    MyBizTheme() {
        RegisterScreen(navController = rememberNavController())
    }
}