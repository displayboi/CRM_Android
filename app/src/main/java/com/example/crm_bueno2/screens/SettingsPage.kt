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

    val textColor = if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
    val modeText = if (isDarkMode) "Modo Claro" else "Modo Oscuro"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header("Configuración")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = modeText,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isDarkMode,
                onCheckedChange = {
                    isDarkMode = it
                    onThemeChange()
                }
            )
        }
    }
}
