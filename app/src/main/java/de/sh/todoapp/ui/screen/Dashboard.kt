package de.sh.todoapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Dashboard composable function for displaying the dashboard screen.
 * This function is responsible for rendering the UI for the dashboard screen, including a button to navigate to the todo list screen.
 */
@Composable
fun Dashboard() {
    // Create a NavController
    val navController = rememberNavController()

    // Define the navigation graph
    NavHost(navController = navController, startDestination = "dashboard") {
        // Define the dashboard composable here
        composable("dashboard") {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { navController.navigate("todo_screen") }) {
                    Text("Todo List", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        // Define the todo_screen composable here
        composable("todo_screen") {
            val context = LocalContext.current
            TodoScreen(
                context = context,
                navController = navController
            )
        }
    }
}
