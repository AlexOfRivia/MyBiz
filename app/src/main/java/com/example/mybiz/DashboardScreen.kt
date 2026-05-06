package com.example.mybiz

//imports for compose charts
import android.R
import android.widget.CheckBox
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.util.Date

data class Income(var amount: Double, var name: String, var date: Date)
data class Spending(var amount: Double, var name: String, var date: Date)


/*TODO
*  change the white color to a sortof cream-ish tint
*  implement adding incomes and expenses
*  implement showing incomes and expenses on chart*/

@Composable
fun DashboardScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var IncomeList = mutableListOf<Income>()
    var SpendingsList = mutableListOf<Spending>()

    var show_dialog by remember { mutableStateOf(false) }

    var testList = mutableListOf<Double>(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)
    var testList2 = mutableListOf<Double>(6.0, 3.5, 3.0, 6.7, 9.8, 4.20, 6.9, 12.0, 9.0, 11.0)
    var currentChartView by remember { mutableStateOf("Przychody") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, top = 15.dp),
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(47, 186, 63)
                )
            }
        }

        var expanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .width(300.dp)
                .padding(top = 40.dp),
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
                    },

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
            ElevatedButton(
                onClick = {
                    show_dialog = true
                },
                modifier = Modifier.height(40.dp).width(60.dp).padding(top=10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
            ) { Text(
                text = "+",
                fontWeight = FontWeight.Bold,
                color = Color.White
            ) }

        }

        LineChart(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            data = when (currentChartView) {
                "Przychody" -> listOf(
                    Line(
                        label = currentChartView,
                        values = testList, //przychody
                        color = SolidColor(Color(0xFF00C853)),
                        firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
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
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
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
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                    Line(
                        label = "Wydatki",
                        values = testList2, //wydatki
                        color = SolidColor(Color(0xFFD50000)),
                        firstGradientFillColor = Color(0xFFCE1111).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
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
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                )
            },
            animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
            //y-axis text
            indicatorProperties = HorizontalIndicatorProperties(
                textStyle = TextStyle(color = Color.White)
            ),
            //label text
            labelHelperProperties = LabelHelperProperties(
                textStyle = TextStyle(color = Color.White)
            )
        )

        //LazyCollumn with all sorts of transactions will go there

        //Operation dialog window handling
        if(show_dialog)
        {
            OperationDialog(onDismissRequest = {show_dialog = false})
        }

    }
}

//Custom dialog window for adding a new operation
@Composable
fun OperationDialog(onDismissRequest: () -> Unit) {
    var title_input by remember { mutableStateOf("") }
    var amount_input by remember { mutableStateOf("") }
    var is_income by remember { mutableStateOf(false) }
    var date_input by remember { mutableStateOf("") } //TRANSFORM TO DATE LATER!!!
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(15.dp),
            shape = RoundedCornerShape(15.dp)
        ) {


            Column(     //main column
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(  //operation title input field
                    value = title_input,
                    onValueChange = { newValue ->
                        if (newValue.isNotEmpty()) {
                            title_input = newValue
                            return@OutlinedTextField
                        }
                        title_input = newValue
                    },
                    shape = RoundedCornerShape(15.dp),
                    label = { Text("Tytuł Operacji:") },
                    singleLine = true,
                    modifier = Modifier.padding(15.dp)
                )

                Row( //operation amount row
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(  //operation title input field
                        value = amount_input,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty())
                            {
                                amount_input = newValue
                                return@OutlinedTextField
                            }
                            amount_input = newValue
                        },
                        shape = RoundedCornerShape(15.dp),
                        label = { Text("Kwota Operacji:") },
                        singleLine = true,
                        modifier = Modifier.padding(15.dp)
                    )
                    Text(
                        text = "PLN",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(15.dp)
                    )

                }

                Row( //income checkbox row
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Checkbox(
                        checked = is_income,
                        onCheckedChange = { isChecked ->
                            is_income = isChecked
                        }
                    )

                    Text(
                        text = "PLN",
                        fontWeight = FontWeight.Bold
                    )
                }

                //Date field

                Row( //dialog buttons row
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton( //dismiss button
                        onClick = { onDismissRequest() },

                    ) {
                        Text(
                            text = "Anuluj",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }

                    TextButton( //accept button
                        onClick = {},

                        ) {
                        Text(
                            text = "Dalej",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MyBizTheme(darkTheme = true) {
        DashboardScreen(navController = rememberNavController())
    }
}