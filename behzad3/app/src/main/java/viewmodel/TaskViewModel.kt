package ir.example.behzadjob.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ir.example.behzadjob.model.AppDatabase
import ir.example.behzadjob.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).taskDao()
    val tasks: Flow<List<Task>> = dao.getAllTasks()

    fun addTask(task: Task) {
        viewModelScope.launch {
            dao.insert(task)
        }
    }
}
