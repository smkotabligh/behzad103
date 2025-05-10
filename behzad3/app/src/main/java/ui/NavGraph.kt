package ir.example.behzadjob.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.example.behzadjob.viewmodel.TaskViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel, navController)
        }
        composable("taskDetail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            taskId?.let {
                TaskDetailScreen(viewModel = viewModel, taskId = it, navController = navController)
            }
        }
    }
}
