package com.example.mybiz

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import java.util.Date
//imports for compose charts
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.*

data class Income(var amount: Double, var name: String, var date: Date)
data class Spending(var amount: Double, var name: String, var dare: Date)


@Composable
fun DashboardScreen(navController: NavController, authViewModel: AuthViewModel = viewModel())
{
    var IncomeList = mutableListOf<Income>()
    var SpendingsList = mutableListOf<Spending>()

    var testList = mutableListOf<Double>(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0)
    var testList2 = mutableListOf<Double>(6.0,3.5,3.0,6.7,9.8,4.20,6.9,12.0,9.0,11.0)
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
                onClick = {
                    authViewModel.signOut()
                    navController.navigate("main_screen")
                }
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

                //expense/income button here
            Button(
                onClick = { /*dialog window with an option to input expense/income*/ },
            ) { Text("+") }

        }

        LineChart(
            modifier = Modifier.width(300.dp).height(300.dp),
            data = when(currentChartView) {
                    "Przychody" -> listOf(
                        Line(
                            label = currentChartView,
                            values = testList, //przychody
                            color = SolidColor(Color(0xFF00C853)),
                            firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = DrawStyle.Stroke(width=2.dp)
                        ),
                    )
                    "Wydatki" -> listOf(
                        Line(
                            label = currentChartView,
                            values = testList2, //wydatki
                            color = SolidColor(Color(0xFFD50000)),
                            firstGradientFillColor = Color(0xFFCE1111).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = DrawStyle.Stroke(width=2.dp)
                        )
                    )
                    "Wszystko" -> listOf(
                        Line(
                            label = "Przychody",
                            values = testList, //przychody
                            color = SolidColor(Color(0xFF00C853)),
                            firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = DrawStyle.Stroke(width=2.dp)
                        ),
                        Line(
                            label = "Wydatki",
                            values = testList2, //wydatki
                            color = SolidColor(Color(0xFFD50000)),
                            firstGradientFillColor = Color(0xFFCE1111).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = DrawStyle.Stroke(width=2.dp)
                        )
                    )
                    //defaultowo przychody
                    else -> listOf(
                        Line(
                            label = "Przychody",
                            values = testList, //przychody
                            color = SolidColor(Color(0xFF00C853)),
                            firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = DrawStyle.Stroke(width=2.dp)
                        ),
                    )
            },
            animationMode = AnimationMode.Together(delayBuilder = {it*500L}),
            //y-axis text
            indicatorProperties = HorizontalIndicatorProperties (
                textStyle = TextStyle(color = Color.White)
            ),
            //label text
            labelHelperProperties = LabelHelperProperties (
                textStyle = TextStyle(color = Color.White)
            )
        )

        //LazyCollumn with all sorts of transactions will go there
        
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