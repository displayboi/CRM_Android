package com.example.crm_bueno2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.crm_bueno2.ui.theme.CRM_bueno2Theme
import com.example.crm_bueno2.navigation.NavGraph
import com.example.crm_bueno2.utils.FirebaseUtils
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var startDestination by remember { mutableStateOf("login") }

            // Verificar si el usuario ya está autenticado
            val currentUser = FirebaseUtils.auth.currentUser
            if (currentUser != null) {
                startDestination = "home"
            }

            CRM_bueno2Theme(darkTheme = isDarkTheme) {
                NavGraph(
                    startDestination = startDestination, // Definimos la pantalla inicial
                    onThemeChange = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}
