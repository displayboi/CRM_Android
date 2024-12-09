package com.example.crm_bueno2.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crm_bueno2.components.Footer
import com.example.crm_bueno2.components.Header
import com.google.firebase.firestore.FirebaseFirestore
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

data class CalendarUiState(
    val yearMonth: YearMonth,
    val dates: List<Date>
) {
    data class Date(
        val dayOfMonth: String,
        val isSelected: Boolean,
        val dateString: String, // Formato "dd/MM/yyyy"
        val hasTasks: Boolean
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarPage(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    val currentYearMonth = remember { mutableStateOf(YearMonth.now()) }
    val selectedDate = remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<String>()) }
    var taskDates by remember { mutableStateOf(setOf<String>()) }

    // Carga las fechas con tareas al cambiar de mes
    LaunchedEffect(currentYearMonth.value) {
        loadTaskDates(db, currentYearMonth.value) { taskDates = it }
    }

    // Carga las tareas del día seleccionado
    LaunchedEffect(selectedDate.value) {
        if (selectedDate.value.isNotEmpty()) {
            loadTasksForDate(db, selectedDate.value) { tasks = it }
        }
    }

    val calendarUiState = generateCalendarUiState(currentYearMonth.value, selectedDate.value, taskDates)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header("Calendario")

        // Navegación entre meses
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                currentYearMonth.value = currentYearMonth.value.minusMonths(1)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }

            Text(
                text = currentYearMonth.value.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge.copy(
                        color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onBackground
                        ),
            )

            IconButton(onClick = {
                currentYearMonth.value = currentYearMonth.value.plusMonths(1)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        // Calendario
        Content(
            dates = calendarUiState.dates,
            onDateClickListener = { date ->
                if (date.dateString.isNotEmpty()) {
                    selectedDate.value = date.dateString
                }
            }
        )

        // Lista de tareas del día seleccionado
        if (tasks.isNotEmpty()) {
            Text(
                text = "Tareas para el ${selectedDate.value}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(tasks) { task ->
                    Text(
                        text = "➟ $task",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)

                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Footer(navController)}
}

@Composable
fun Header(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onBackground
                    ),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun loadTaskDates(db: FirebaseFirestore, yearMonth: YearMonth, callback: (Set<String>) -> Unit) {
    db.collection("agenda")
        .whereGreaterThanOrEqualTo("date", "01/${"%02d".format(yearMonth.monthValue)}/${yearMonth.year}")
        .whereLessThanOrEqualTo("date", "${yearMonth.lengthOfMonth()}/${"%02d".format(yearMonth.monthValue)}/${yearMonth.year}")
        .get()
        .addOnSuccessListener { result ->
            val taskDateSet = result.mapNotNull { it.getString("date") }.toSet()
            callback(taskDateSet)
        }
}

fun loadTasksForDate(db: FirebaseFirestore, date: String, callback: (List<String>) -> Unit) {
    db.collection("agenda")
        .whereEqualTo("date", date)
        .get()
        .addOnSuccessListener { result ->
            val taskList = result.mapNotNull { it.getString("task") }
            callback(taskList)
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateCalendarUiState(
    yearMonth: YearMonth,
    selectedDate: String,
    taskDates: Set<String>
): CalendarUiState {
    val dates = mutableListOf<CalendarUiState.Date>()
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7
    val totalDays = yearMonth.lengthOfMonth()

    // Días vacíos antes del inicio del mes
    repeat(firstDayOfMonth) {
        dates.add(CalendarUiState.Date("", false, "", false))
    }

    // Días del mes
    for (day in 1..totalDays) {
        val dateString = "${"%02d".format(day)}/${"%02d".format(yearMonth.monthValue)}/${yearMonth.year}"
        dates.add(
            CalendarUiState.Date(
                dayOfMonth = day.toString(),
                isSelected = dateString == selectedDate,
                dateString = dateString,
                hasTasks = dateString in taskDates
            )
        )
    }

    return CalendarUiState(yearMonth, dates)
}

@Composable
fun Content(
    dates: List<CalendarUiState.Date>,
    onDateClickListener: (CalendarUiState.Date) -> Unit
) {
    Column {
        var index = 0
        repeat(6) {
            if (index >= dates.size) return@repeat
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) {
                    if (index < dates.size) {
                        val date = dates[index]
                        ContentItem(
                            date = date,
                            onClickListener = onDateClickListener,
                            modifier = Modifier.weight(1f)
                        )
                        index++
                    }
                }
            }
        }
    }
}

@Composable
fun ContentItem(
    date: CalendarUiState.Date,
    onClickListener: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(
                color = if (date.isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
            )
            .clickable { onClickListener(date) },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(8.dp)
            )
            if (date.hasTasks) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                )
            }
        }
    }
}

