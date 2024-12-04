package com.example.crm_bueno2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crm_bueno2.components.Header

@Composable
fun SettingsPage(onThemeChange: () -> Unit) {
    var isDarkMode by remember { mutableStateOf(false) }

    Column( modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background).padding(16.dp)) {
        Header("Configuración")
        Text("Modo Oscuro")
        Switch(
            checked = isDarkMode,
            onCheckedChange = {
                isDarkMode = it
                onThemeChange()
            }
        )

    }
}
