package com.example.crm_bueno2.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Footer
import com.example.crm_bueno2.components.Header
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomePage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email
    val username = email?.substringBefore("@") ?: "Usuario"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top
    ) {
        Header("Home")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bienvenido, $username",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("agenda") }) {
                Text("Ir a Agenda")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("calendar") }) {
                Text("Ir a Calendario")
            }
            Spacer(modifier = Modifier.weight(1f))
            Footer(navController)
        }
    }
}
