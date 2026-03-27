package com.example.mybiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybiz.ui.theme.MyBizTheme

@Composable
fun MainScreen()
{

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "MyBiz",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 70.dp, bottom = 200.dp)
        )

        Button(
            onClick = { /*przeniesienie na ekran logowania*/ },
            modifier = Modifier.fillMaxWidth().padding(40.dp)
        ) {
            Text("Zaloguj się")
        }

        Button(
            onClick = { /*przeniesienie na ekran logowania*/ },
            modifier = Modifier.fillMaxWidth().padding(40.dp)
        ) {
            Text("Zarejestruj się")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    MyBizTheme() {
        MainScreen()
    }
}