package ir.example.behzadjob.ui

import android.app.DatePickerDialog
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
fun TaskDetailScreen(viewModel: TaskViewModel, taskId: Int, navController: NavController) {
    val task = viewModel.getTaskById(taskId)?.let { it.copy() } ?: return
    var title by remember { mutableStateOf(task.title) }
    var dueDate by remember { mutableStateOf(LocalDate.parse(task.dueDate)) }

    val context = LocalContext.current
    val datePicker = DatePickerDialog(
        context,
        { _, y, m, d -> dueDate = LocalDate.of(y, m + 1, d) },
        dueDate.year, dueDate.monthValue - 1, dueDate.dayOfMonth
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("ویرایش کار", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("عنوان") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Text("تاریخ: $dueDate")
        Button(onClick = { datePicker.show() }) { Text("تغییر تاریخ") }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.updateTask(task.copy(title = title, dueDate = dueDate.toString()))
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("ذخیره تغییرات")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            viewModel.deleteTask(task)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
            Text("🗑 حذف کار")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // در آینده وصل به تایمر پومودورو
        }, modifier = Modifier.fillMaxWidth()) {
            Text("🎯 تمرکز")
        }
    }
}
