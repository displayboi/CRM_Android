package com.example.crm_bueno2.components



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Footer(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { navController.navigate("messages") }) {
            Text("✉")
        }
        Button(onClick = { navController.navigate("home") }) {
            Text("\uD83C\uDFE0")
        }
        Button(onClick = { navController.navigate("settings") }) {
            Text("⚙\uFE0F")
        }
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login") {
                popUpTo(0) // Limpia el stack de navegación.
            }
        }) {
            Text("🚪")
        }
    }
}
