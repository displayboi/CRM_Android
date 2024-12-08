package com.example.crm_bueno2.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.utils.FirebaseUtils

@Composable
fun RegisterPage(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            FirebaseUtils.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    message = if (task.isSuccessful) {
                        "User Registered Successfully"
                    } else {
                        task.exception?.message ?: "Error"
                    }
                }
        }) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("login") }) {
            Text("Back to Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}

