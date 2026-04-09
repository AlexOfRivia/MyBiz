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

@Composable
fun DashboardScreen(navController: NavController)
{

}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview()
{
    MyBizTheme(darkTheme = true) {
        DashboardScreen(navController = rememberNavController())
    }
}