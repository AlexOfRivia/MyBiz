package com.example.mybiz

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "Incomes")      //initializing a new table
data class Income(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var amount: Double,
    var name: String,
    var date: LocalDate
)

@Entity(tableName = "Expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)        //auto-incremented id as primary key
    val id: Long = 0,
    var amount: Double,
    var name: String,
    var date: LocalDate
)

@Composable
fun DashboardScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(context)           //db related stuff
    val incomeDAO = db.IncomeDAO()
    val expenseDAO = db.ExpenseDAO()

    val username = authViewModel.username

    val incomeList = remember { mutableStateListOf<Income>() }
    val expenseList = remember { mutableStateListOf<Expense>() }

    LaunchedEffect(Unit)        //unit makes sure that this will only execute upon startup
    {
        withContext(Dispatchers.IO)
        {
            incomeDAO.getAllIncomes()
                .collect { allIncomes ->       //collecting data from the flow basically
                    incomeList.clear()
                    incomeList.addAll(allIncomes)
                }
        }
    }

    LaunchedEffect(Unit)
    {
        withContext(Dispatchers.IO)
        {
            expenseDAO.getAllExpenses().collect { allExpenses ->
                expenseList.clear()
                expenseList.addAll(allExpenses)
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var currentChartView by remember { mutableStateOf("Przychody") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Akcje użytkownika",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(47,186,63)
                )
                HorizontalDivider(modifier = Modifier.padding(bottom = 80.dp))

                NavigationDrawerItem(
                    label = {
                        Text(
                            color = Color(47, 186, 63),
                            fontWeight = FontWeight.Bold,
                            text = "Edytuj Konto"
                    ) },
                    selected = false,
                    onClick = {
                        navController.navigate("user_info_screen")
                    }
                )

                HorizontalDivider(
                    color = Color.Transparent,
                    modifier = Modifier.padding(150.dp)
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            color = Color(47, 186, 63),
                            fontWeight = FontWeight.Bold,
                            text = "Wyloguj się"
                        ) },
                    selected = false,
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate("main_screen")
                    }
                )
            }
        }
    ) {
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
                    text = "Witaj, $username",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(20.dp)
                )

                    IconButton(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        },
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "User options",
                            tint = Color(47, 186, 63),
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
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

                //expense/income button
                ElevatedButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
                ) {
                    Text(
                        text = "+",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            }

            val incomeValues = incomeList.map { it.amount }
            val expenseValues = expenseList.map { it.amount }

            val hasData = when (currentChartView) {
                "Przychody" -> incomeValues.isNotEmpty()
                "Wydatki" -> expenseValues.isNotEmpty()
                "Wszystko" -> incomeValues.isNotEmpty() || expenseValues.isNotEmpty()
                else -> incomeValues.isNotEmpty()
            }

            if (!hasData) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(350.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(22, 22, 26)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Brak danych do pokazania :( ",
                        color = Color(47, 186, 63),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LineChart(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(350.dp)
                        .height(300.dp),
                    data = remember(
                        currentChartView,
                        incomeValues,
                        expenseValues
                    ) { //adding the remember makes sure, that the chart will refresh only when one of these three changes
                        val lines = mutableListOf<Line>() //chart lines

                        //income line
                        if ((currentChartView == "Przychody" || currentChartView == "Wszystko") && incomeValues.isNotEmpty()) {
                            lines.add(
                                Line(
                                    label = "Przychody",
                                    values = incomeValues,
                                    color = SolidColor(Color(0xFF00C853)),
                                    firstGradientFillColor = Color(0xFF02A143).copy(alpha = .5f),
                                    secondGradientFillColor = Color.Transparent,
                                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                    drawStyle = DrawStyle.Stroke(width = 2.dp)
                                )
                            )
                        }

                        //expense line
                        if ((currentChartView == "Wydatki" || currentChartView == "Wszystko") && expenseValues.isNotEmpty()) {
                            lines.add(
                                Line(
                                    label = "Wydatki",
                                    values = expenseValues,
                                    color = SolidColor(Color(0xFFD50000)),
                                    firstGradientFillColor = Color(0xFFCE1111).copy(alpha = .5f),
                                    secondGradientFillColor = Color.Transparent,
                                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                    drawStyle = DrawStyle.Stroke(width = 2.dp)
                                )
                            )
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
            ) {
                LazyColumn(
                    //transaction lazy column
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    //checking for currently selected view option
                    if ((currentChartView == "Przychody" || currentChartView == "Wszystko") && incomeValues.isNotEmpty()) {
                        //showing income list
                        items(incomeList) { item -> //for every item in list:
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(340.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(36, 36, 36)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
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
                                        text = (item.amount.toString() + "PLN"),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(0.2f)
                                    )
                                }
                            }
                        }
                    }
                    if ((currentChartView == "Wydatki" || currentChartView == "Wszystko") && expenseValues.isNotEmpty()) {
                        //showing expense items
                        items(expenseList) { item -> //for every item in list:
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(340.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(36, 36, 36)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
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
                                        text = "-" + (item.amount.toString() + "PLN"),
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
            if (showDialog) {
                OperationDialog(
                    onDismissRequest = { showDialog = false },
                    incomes = incomeList,
                    expenses = expenseList,
                    incomeDAO = incomeDAO,
                    expenseDAO = expenseDAO
                )
            }

        }
    }
}

//Custom dialog window for adding a new operation
@Composable
fun OperationDialog(
    onDismissRequest: () -> Unit,
    incomes: MutableList<Income>,
    expenses: MutableList<Expense>,
    incomeDAO: IncomeDAO,
    expenseDAO: ExpenseDAO
) {

    val coroutineScope = rememberCoroutineScope()


    var titleInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var dateInput by remember { mutableStateOf(LocalDate.now()) }

    val amountRegex = Regex("^[+]?([0-9]+(?:[.][0-9]{0,2})?|\\.[0-9]{0,2})$")

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
                    value = titleInput,
                    onValueChange = { newValue ->
                        if (newValue.isNotEmpty()) {
                            titleInput = newValue
                            return@OutlinedTextField
                        }
                        titleInput = newValue
                    },
                    shape = RoundedCornerShape(15.dp),
                    label = { Text("Tytuł Operacji:") },
                    singleLine = true,
                    modifier = Modifier.padding(15.dp)
                )

                Row(
                    //operation amount row
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(  //operation title input field
                        value = amountInput,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty()) {
                                amountInput = newValue
                                return@OutlinedTextField
                            }
                            if (newValue.matches(amountRegex)) {
                                amountInput = newValue
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
                        checked = isIncome,
                        onCheckedChange = { isChecked ->
                            isIncome = isChecked
                        }
                    )

                    Text(
                        text = "Przychód?",
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                DatePickerField(
                    "Data operacji",
                    dateInput,
                    onDateSelected = { dateInput = it }) //operation date field

                Row( //dialog buttons row
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton( //dismiss button
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }

                    TextButton( //accept button
                        onClick = {
                            if (!titleInput.isEmpty() && !amountInput.isEmpty()) {

                                if (isIncome) {
                                    val newIncome = Income(
                                        amount = amountInput.toDouble(),
                                        name = titleInput,
                                        date = dateInput
                                    )
                                    incomes.add(newIncome)

                                    coroutineScope.launch(Dispatchers.IO)
                                    {
                                        incomeDAO.insert(newIncome)
                                    }
                                    Toast.makeText(
                                        context,
                                        "Przychód dodany pomyślnie!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    val newExpense = Expense(
                                        amount = amountInput.toDouble(),
                                        name = titleInput,
                                        date = dateInput
                                    )
                                    expenses.add(newExpense)

                                    coroutineScope.launch(Dispatchers.IO)
                                    {
                                        expenseDAO.insert(newExpense)
                                    }
                                    //Toast.makeText(context, "Wydatek dodany pomyślnie!", Toast.LENGTH_SHORT).show()
                                }
                                onDismissRequest()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Wszystkie pola muszą być uzupełnione!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(10.dp)

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
                color = Color(47, 186, 63)
            )
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