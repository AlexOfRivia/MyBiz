package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybiz.ui.theme.MyBizTheme

@Composable
fun RegisterScreen()
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

        //3 text fields here

        //register button will go here lateron


    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview()
{
    MyBizTheme() {
        RegisterScreen()
    }
}