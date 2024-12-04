package com.example.crm_bueno2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Footer
import com.example.crm_bueno2.components.Header

@Composable
fun HomePage(navController: NavController) {
    Column( modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background).padding(16.dp)) {
        Header("Página Principal")
        Button(onClick = { navController.navigate("agenda") }) {
            Text("Ir a Agenda")
        }
        Button(onClick = { navController.navigate("calendar") }) {
            Text("Ir a Calendario")
        }
        Footer(navController)
    }
}
