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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AgendaPage(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var task by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Pair<String, String>>()) }

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
            TextField(
                value = task,
                onValueChange = { task = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val taskMap = hashMapOf("task" to task)
                db.collection("agenda").add(taskMap).addOnSuccessListener {
                    task = ""
                    loadTasks(db) { tasks = it }
                }
            }) {
                Text("Añadir tarea")
            }
            Spacer(modifier = Modifier.height(16.dp))
            tasks.forEach { (id, taskText) ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(taskText, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        db.collection("agenda").document(id).delete().addOnSuccessListener {
                            loadTasks(db) { tasks = it }
                        }
                    }) {
                        Text("✔\uFE0E")
                    }
                }
            }
        }

        Footer(navController)
    }
}

fun loadTasks(db: FirebaseFirestore, callback: (List<Pair<String, String>>) -> Unit) {
    db.collection("agenda").get().addOnSuccessListener { result ->
        val taskList = result.map { it.id to (it.getString("task") ?: "") }
        callback(taskList)
    }
}
