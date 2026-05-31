package com.example.mybiz

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybiz.ui.theme.MyBizTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth

@Composable
fun UserInfoScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {

    var newUsername by remember { mutableStateOf(authViewModel.username) }
    var oldUserPassword by remember { mutableStateOf("") }
    var newUserPassword by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val auth = Firebase.auth
    val user = auth.currentUser

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 75.dp, top = 15.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            ElevatedButton(
                onClick = {
                    navController.navigate("dashboard_screen")
                },
                modifier = Modifier
                    .width(125.dp)
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
            ) {
                Text(
                    text = "Powrót",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        OutlinedTextField(      //username input
            value = newUsername,
            onValueChange = { newValue ->
                newUsername = newValue
            },
            label = { Text("Nazwa użytkownika") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(      //password input
            value = oldUserPassword,
            onValueChange = { oldUserPassword = it },
            label = { Text("Stare Hasło") },
            modifier = Modifier.padding(bottom = 40.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(      //repeat password input
            value = newUserPassword,
            onValueChange = { newUserPassword = it },
            label = { Text("Nowe Hasło") },
            modifier = Modifier.padding(bottom = 90.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        ElevatedButton(                    //save changes button
            onClick = {
                if(newUsername == authViewModel.username)
                {
                    Toast.makeText(context, "Nowa nazwa konta musi być inna od starej", Toast.LENGTH_LONG).show()
                } else {
                    if(newUsername.isNotEmpty())
                    {
                        authViewModel.updateUsername(newUsername)
                        navController.navigate("dashboard_screen")
                        Toast.makeText(context, "Nazwa użytkownika zmieniona pomyślnie!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Nazwa nie może być pusta!", Toast.LENGTH_LONG).show()
                    }
                }



                if (oldUserPassword.isNotEmpty() && newUserPassword.isNotEmpty()) {
                    val credential = EmailAuthProvider.getCredential(user?.email!!, oldUserPassword)
                    if (oldUserPassword==(newUserPassword))
                    {
                        Toast.makeText(context, "Hasła nie mogą być takie same", Toast.LENGTH_SHORT).show()
                    } else {
                        user.reauthenticate(credential)
                            .addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    user.updatePassword(newUserPassword)
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful)
                                            {
                                                Toast.makeText(context,"Hasło zmienione pomyślnie",Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Error: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Błąd indentyfikacji, spróbuj ponownie później", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            },
            modifier = Modifier.padding(bottom = 150.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(47, 186, 63))
        ) {
            Text(
                text = "Zapisz",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }


        //Delete account button with "We're sad to see you go. are you sure you want to?" dialog
        TextButton(
            onClick = { showDialog = true }
        ) {
            Text(
                text = "Usuń konto",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showDialog)
    {
        DeleteUserDialog(onDismissRequest = { showDialog = false }, authViewModel, navController)
    }
}

@Composable
fun DeleteUserDialog(
    onDismissRequest: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    //NOTES: after clicking the OK button -> delete account + navigate to main screen
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .width(400.dp)
                .padding(15.dp),
            shape = RoundedCornerShape(15.dp)
        ) {

            Column(         //main column
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //confirmation text
                Text(
                    text = "Przykro nam, że odchodzisz. Czy na pewno chcesz usunąć konto?",
                    color = Color.White,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    TextButton(         //dismiss button
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }
                    TextButton(          //accept button
                        onClick = {
                            authViewModel.deleteUser()
                            navController.navigate("main_screen")
                        },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "OK",
                            fontWeight = FontWeight.Bold,
                            color = Color(47, 186, 63)
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun UserInfoScreenPreview() {
    MyBizTheme(darkTheme = true) {
        UserInfoScreen(navController = rememberNavController())
    }
}