package com.example.crm_bueno2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Footer
import com.example.crm_bueno2.components.Header
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaPage(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var task by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") } // Almacena la fecha seleccionada
    val openDatePickerDialog = remember { mutableStateOf(false) } // Controla la visibilidad del diálogo

    var tasks by remember { mutableStateOf(listOf<Map<String, String>>()) }

    LaunchedEffect(Unit) {
        loadTasks(db) { tasks = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header("Agenda")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Campo para nueva tarea
            TextField(
                value = task,
                onValueChange = { task = it },
                label = { Text("Nueva tarea", color = MaterialTheme.colorScheme.onBackground) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Selector de fecha
            Button(onClick = { openDatePickerDialog.value = true }) {
                Text(
                    text = if (selectedDate.isNotEmpty()) "Fecha: $selectedDate" else "Seleccionar fecha",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para añadir tarea
            Button(onClick = {
                if (task.isNotEmpty() && selectedDate.isNotEmpty()) {
                    val taskMap = hashMapOf(
                        "task" to task,
                        "date" to selectedDate
                    )
                    db.collection("agenda").add(taskMap).addOnSuccessListener {
                        task = ""
                        selectedDate = ""
                        loadTasks(db) { tasks = it }
                    }
                }
            }) {
                Text("Añadir tarea")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de tareas
            tasks.forEach { taskMap ->
                val taskText = taskMap["task"] ?: ""
                val taskDate = taskMap["date"] ?: ""
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(
                        "$taskText - $taskDate",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Button(onClick = {
                        val id = taskMap["id"] ?: return@Button
                        db.collection("agenda").document(id).delete().addOnSuccessListener {
                            loadTasks(db) { tasks = it }
                        }
                    }) {
                        Text("✔\uFE0E")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Footer(navController)}



        // Diálogo de selección de fecha
        if (openDatePickerDialog.value) {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }
            DatePickerDialog(
                onDismissRequest = { openDatePickerDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog.value = false
                            val selectedMillis = datePickerState.selectedDateMillis ?: 0L
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            selectedDate = formatter.format(Date(selectedMillis))
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDatePickerDialog.value = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

fun loadTasks(db: FirebaseFirestore, callback: (List<Map<String, String>>) -> Unit) {
    db.collection("agenda").get().addOnSuccessListener { result ->
        val taskList = result.map { document ->
            mapOf(
                "id" to document.id,
                "task" to (document.getString("task") ?: ""),
                "date" to (document.getString("date") ?: "")
            )
        }
        callback(taskList)
    }
}
