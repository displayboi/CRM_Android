package com.example.crm_bueno2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Header
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginPage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(

        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(

            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
        Header("Inicio de sesión")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (email.isBlank()) {

                errorMessage = "El correo no puede estar vacío"


            } else if (password.isBlank()) {

                errorMessage =  "La contraseña no puede estar vacía"


            } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate("home")
                    } else {
                        errorMessage = task.exception?.message
                    }
                }}
        }) {
            Text("Iniciar sesion")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("register") }) {
            Text("Ir a registro")
        }
        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}}