package com.example.mybiz

import android.widget.Spinner
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
import java.util.Date
import kotlin.math.exp

data class Income(var amount: Double, var name: String, var date: Date)
data class Spending(var amount: Double, var name: String, var dare: Date)


@Composable
fun DashboardScreen(navController: NavController)
{
    var IncomeList = mutableListOf<Income>()
    var SpendingsList = mutableListOf<Spending>()

    var testList = mutableListOf<Double>(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0)
    var currentChartView by remember { mutableStateOf("Przychody") }

    //this dashboard will have charts etc.
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp, top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Witaj, imię",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(10.dp)
            )

            TextButton(
                onClick = {navController.navigate("main_screen")},
            ) {
                Text(
                    text = "Wyloguj się",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        var expanded by remember {mutableStateOf(false)}

        Row(
            modifier = Modifier.width(300.dp).padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Przychody") },
                        onClick = {
                            currentChartView = "Przychody"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Wydatki") },
                        onClick = {
                            currentChartView = "Wydatki"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Wszystko") },
                        onClick = {
                            currentChartView = "Wszystko"
                            expanded = false
                        }
                    )
                }



        }

        LineChart(
            modifier = Modifier.width(300.dp).height(300.dp),
            data = remember(currentChartView) {
                listOf(
                    Line(
                        label = currentChartView,
                        values = testList,
                        color = SolidColor(Color(0xFF23af92)),
                        firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width=2.dp)
                    )
                )
            },
            animationMode = AnimationMode.Together(delayBuilder = {it*500L})
        )
        
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview()
{
    MyBizTheme(darkTheme = true) {
        DashboardScreen(navController = rememberNavController())
    }
}