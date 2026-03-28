package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybiz.ui.theme.MyBizTheme

@Composable
fun LoginScreen()
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Zaloguj się",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 70.dp, bottom = 200.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                /*zmiana maila + regex*/
            },
            label = { Text("Podaj email") },
            modifier = Modifier.padding(bottom = 50.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                /*zmiana hasła + regex*/
            },
            label = { Text("Podaj hasło") },
            modifier = Modifier.padding(bottom = 60.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                /*przejście do ekranu głównego*/
            },
            modifier = Modifier.width(200.dp).padding(bottom=50.dp, top = 60.dp)
        ) {
            Text("Dalej")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview()
{
    MyBizTheme() {
        LoginScreen()
    }
}