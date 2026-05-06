package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.google.firebase.FirebaseApp.*
import com.google.firebase.auth.*

@Composable
fun MainScreen(navController: NavController)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "MyBiz",
            color = Color(47, 186, 63),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 90.dp, bottom = 200.dp)
        )

        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier.fillMaxWidth().padding(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
        ) {
            Text(
                text = "Zaloguj się",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Button(
            onClick = { navController.navigate("register_screen") },
            modifier = Modifier.fillMaxWidth().padding(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
        ) {
            Text(
                text = "Zarejestruj się",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    MyBizTheme(darkTheme = true) {
        MainScreen(navController = rememberNavController())
    }
}