package com.example.crm_bueno2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Footer(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).background(MaterialTheme.colorScheme.background)) {
        Button(onClick = { navController.navigate("messages") }, modifier = Modifier.weight(1f)) {
            Text("✉")
        }

        Button(onClick = { navController.navigate("home") }, modifier = Modifier.weight(1f)) {
            Text("\uD83C\uDFE0")
        }

        Button(onClick = { navController.navigate("settings") }, modifier = Modifier.weight(1f)) {
            Text("⚙\uFE0F")
        }
    }
}
