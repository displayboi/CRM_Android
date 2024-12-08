package com.example.crm_bueno2.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Footer

@Composable
fun HomePage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
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