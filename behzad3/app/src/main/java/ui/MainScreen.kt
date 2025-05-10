package ir.example.behzadjob.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.example.behzadjob.model.Task
import ir.example.behzadjob.viewmodel.TaskViewModel
import java.time.LocalDate

@Composable
fun MainScreen(viewModel: TaskViewModel, navController: NavController) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    var tab by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    val today = LocalDate.now()
    val todayTasks = tasks.filter { it.dueDate == today.toString() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    label = { Text("امروز") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    label = { Text("اهداف") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    label = { Text("منو") },
                    icon = {}
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                title = ""
                selectedDate = today
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (tab) {
                0 -> {
                    Text("امروز", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (todayTasks.isEmpty()) {
                        Text("هیچ کاری برای امروز ثبت نشده.")
                    } else {
                        todayTasks.forEach { task ->
                            Text(
                                text = "• ${task.title}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("taskDetail/${task.id}") }
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                1 -> {
                    Text("اهداف", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (tasks.isEmpty()) {
                        Text("هنوز هیچ کاری ثبت نشده.")
                    } else {
                        tasks.forEach { task ->
                            Text(
                                text = "• ${task.title} - ${task.dueDate}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("taskDetail/${task.id}") }
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                2 -> {
                    Text("منو", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("افزودن کار") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("عنوان") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("تاریخ انتخابی: $selectedDate")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { datePickerDialog.show() }) {
                            Text("انتخاب تاریخ")
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                val task = Task(title = title, dueDate = selectedDate.toString())
                                viewModel.addTask(task)
                                showDialog = false
                            }
                        }
                    ) {
                        Text("ذخیره")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("لغو")
                    }
                }
            )
        }
    }
}
