package com.example.mybiz

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybiz.ui.theme.MyBizTheme
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "Incomes")      //initializing a new table
data class Income (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var amount: Double,
    var name: String,
    var date: LocalDate
)

@Entity(tableName = "Expenses")
data class Expense (
    @PrimaryKey(autoGenerate = true)        //auto-incremented id as primary key
    val id: Long = 0,
    var amount: Double,
    var name: String,
    var date: LocalDate
)

/*TODO
*  implement the Room database and saving user info to Firebase
*  Add a spinwheel while loading data from db
*/

@Composable
fun DashboardScreen(navController: NavController, authViewModel: AuthViewModel = viewModel())
{
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(context)           //db related stuff
    val incomeDAO = db.IncomeDAO()
    val expenseDAO = db.ExpenseDAO()

    val IncomeList = remember { mutableStateListOf<Income>() }
    val ExpenseList = remember { mutableStateListOf<Expense>() }

    LaunchedEffect(Unit)        //unit makes sure that this will only execute upon startup
    {
        withContext(Dispatchers.IO)
        {
            incomeDAO.getAllIncomes().collect { allIncomes ->       //collecting data from the flow basically
                IncomeList.clear()
                IncomeList.addAll(allIncomes)
            }
        }
    }

    LaunchedEffect(Unit)
    {
        withContext(Dispatchers.IO)
        {
            expenseDAO.getAllExpenses().collect { allExpenses ->
                ExpenseList.clear()
                ExpenseList.addAll(allExpenses)
            }
        }
    }

    var show_dialog by remember { mutableStateOf(false) }
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
                color = Color.White,
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
                .width(350.dp)
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
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
            ) { Text(
                text = "+",
                fontWeight = FontWeight.Bold,
                color = Color.White
            ) }

        }

        val incomeValues = IncomeList.map { it.amount }
        val expenseValues = ExpenseList.map { it.amount }

        val hasData = when (currentChartView) {
            "Przychody" -> incomeValues.isNotEmpty()
            "Wydatki" -> expenseValues.isNotEmpty()
            "Wszystko" -> incomeValues.isNotEmpty() || expenseValues.isNotEmpty()
            else -> incomeValues.isNotEmpty()
        }

        if(!hasData)
        {
            Box(
                modifier = Modifier
                    .padding(bottom=10.dp)
                    .width(350.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(22, 22, 26)),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Brak danych do pokazania :(", color = Color(47, 186, 63), fontWeight = FontWeight.Bold)
            }
        } else {
            LineChart(
                modifier = Modifier
                    .padding(bottom=10.dp)
                    .width(350.dp)
                    .height(300.dp),
                data = remember(currentChartView, incomeValues, expenseValues) { //adding the remember makes sure, that the chart will refresh only when one of these three changes
                    val lines = mutableListOf<Line>() //chart lines

                    //income line
                    if ((currentChartView == "Przychody" || currentChartView == "Wszystko") && incomeValues.isNotEmpty())
                    {
                        lines.add(Line(
                            label = "Przychody",
                            values = incomeValues,
                            color = SolidColor(Color(0xFF00C853)),
                            firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            drawStyle = DrawStyle.Stroke(width = 2.dp)
                        ))
                    }

                    //expense line
                    if ((currentChartView == "Wydatki" || currentChartView == "Wszystko") && expenseValues.isNotEmpty()) {
                        lines.add(Line(
                            label = "Wydatki",
                            values = expenseValues,
                            color = SolidColor(Color(0xFFD50000)),
                            firstGradientFillColor = Color(0xFFCE1111).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                            drawStyle = DrawStyle.Stroke(width = 2.dp)
                        ))
                    }
                    lines
                },
                animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
                indicatorProperties = HorizontalIndicatorProperties(textStyle = TextStyle(color = Color.White)),
                labelHelperProperties = LabelHelperProperties(textStyle = TextStyle(color = Color.White)),
            )
        }

        Box(
            modifier = Modifier
                .width(350.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(22, 22, 26)),
            contentAlignment = Alignment.Center
        ){
            LazyColumn(     //transaction lazy column
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                //checking for currently selected view option
                if((currentChartView == "Przychody" || currentChartView == "Wszystko") && incomeValues.isNotEmpty())
                {
                    //showing income list
                    items(IncomeList) { item -> //for every item in list:
                        Box(
                            modifier = Modifier
                                .padding(top=4.dp)
                                .width(340.dp)
                                .height(30.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(36, 36, 36)),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(0.4f)
                                )

                                Text(
                                    text = item.date.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(0.2f)
                                )

                                Text(
                                    text = (item.amount.toString()+"PLN"),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(0.2f)
                                )
                            }
                        }
                    }
                }
                if((currentChartView == "Wydatki" || currentChartView == "Wszystko") && expenseValues.isNotEmpty())
                {
                    //showing expense items
                    items(ExpenseList) { item -> //for every item in list:
                        Box(
                            modifier = Modifier
                                .padding(top=4.dp)
                                .width(340.dp)
                                .height(30.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(36, 36, 36)),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(0.4f)
                                )

                                Text(
                                    text = item.date.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(0.2f)
                                )

                                Text(
                                    text = "-"+(item.amount.toString()+"PLN"),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(0.2f)
                                )
                            }
                        }
                    }
                }

            }
        }

        //Operation dialog window handling
        if(show_dialog)
        {
            OperationDialog(onDismissRequest = {show_dialog = false}, IncomeList = IncomeList,ExpenseList =  ExpenseList, incomeDAO = incomeDAO, expenseDAO = expenseDAO)
        }

    }
}

//Custom dialog window for adding a new operation
@Composable
fun OperationDialog(
    onDismissRequest: () -> Unit,
    IncomeList: MutableList<Income>,
    ExpenseList: MutableList<Expense>,
    incomeDAO: IncomeDAO,
    expenseDAO: ExpenseDAO
) {

    val coroutineScope = rememberCoroutineScope()


    var title_input by remember { mutableStateOf("") }
    var amount_input by remember { mutableStateOf("") }
    var is_income by remember { mutableStateOf(false) }
    var date_input by remember { mutableStateOf(LocalDate.now()) }

    val amount_regex =Regex("^[+]?([0-9]+(?:[\\.][0-9]{0,2})?|\\.[0-9]{0,2})$")

    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .width(500.dp)
                .padding(15.dp),
            shape = RoundedCornerShape(15.dp)
        ) {


            Column(    //main column
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
                            if(newValue.matches(amount_regex))
                            {
                                amount_input = newValue
                            }
                        },
                        shape = RoundedCornerShape(15.dp),
                        label = { Text("Kwota Operacji:") },
                        singleLine = true,
                        modifier = Modifier
                            .padding(15.dp)
                            .weight(0.7f)
                    )
                    Text(
                        text = "PLN",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .weight(0.3f),
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
                        text = "Przychód?",
                        modifier = Modifier.padding(top=10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                DatePickerField("Data operacji", date_input, onDateSelected = {date_input = it}) //operation date field

                Row( //dialog buttons row
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton( //dismiss button
                        onClick = { onDismissRequest() }
                    ) {
                        Text(
                            text = "Anuluj",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }

                    TextButton( //accept button
                        onClick = {
                            if(!title_input.isEmpty() && !amount_input.isEmpty())
                            {

                                if(is_income)
                                {
                                    val newIncome = Income(amount = amount_input.toDouble(), name = title_input, date = date_input)
                                    IncomeList.add(newIncome)

                                    coroutineScope.launch(Dispatchers.IO)
                                    {
                                        incomeDAO.insert(newIncome)
                                    }
                                    Toast.makeText(context, "Przychód dodany pomyślnie!", Toast.LENGTH_SHORT).show()

                                } else {
                                    val newExpense = Expense(amount = amount_input.toDouble(), name = title_input, date = date_input)
                                    ExpenseList.add(newExpense)

                                    coroutineScope.launch(Dispatchers.IO)
                                    {
                                        expenseDAO.insert(newExpense)
                                    }
                                    //Toast.makeText(context, "Wydatek dodany pomyślnie!", Toast.LENGTH_SHORT).show()
                                }
                                onDismissRequest()
                            } else {
                                Toast.makeText(context, "Wszystkie pola muszą być uzupełnione!", Toast.LENGTH_LONG).show()
                            }
                        }

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

@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    OutlinedButton(
        onClick = {
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selected = LocalDate.of(year, month + 1, dayOfMonth)
                    onDateSelected(selected)
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            )
            datePicker.show()
        },
        modifier = Modifier
            .padding(bottom = 40.dp)
            .width(300.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(47, 186, 63))
            Text(
                text = selectedDate.format(formatter),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
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