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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.unit.dp
import com.example.crm_bueno2.utils.FirebaseUtils


@Composable
fun AgendaPage(navController: NavController) {
    var task by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Column( modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Header("Agenda")
        Footer(navController)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = task,
            onValueChange = { task = it },
            label = { Text("New Task") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val userId = FirebaseUtils.auth.currentUser?.uid ?: ""
            val taskRef = FirebaseUtils.database.getReference("tasks").child(userId)
            taskRef.push().setValue(task).addOnCompleteListener { task ->
                message = if (task.isSuccessful) {
                    "Task Added Successfully"
                } else {
                    task.exception?.message ?: "Error"
                }
            }
        }) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}}